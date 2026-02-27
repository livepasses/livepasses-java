package com.livepasses.sdk.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.livepasses.sdk.exceptions.ExceptionFactory;
import com.livepasses.sdk.exceptions.LivepassesException;
import com.livepasses.sdk.types.ApiPagedResponse;
import com.livepasses.sdk.types.ApiResponse;
import com.livepasses.sdk.types.PagedResponse;
import com.livepasses.sdk.types.PaginationMetadata;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Internal HTTP client that wraps java.net.http.HttpClient with:
 * - API key injection
 * - ApiResponse envelope unwrapping
 * - Typed error mapping
 * - Automatic retry with exponential backoff for 429 and 5xx
 */
public class LivepassesHttpClient {

    private final String apiKey;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LivepassesHttpClient(String apiKey, String baseUrl, Duration timeout, int maxRetries) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /** Visible for testing. */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T get(String path, Map<String, String> params, Class<T> responseType) {
        return request("GET", path, params, null, responseType);
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return request("POST", path, null, body, responseType);
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return request("PUT", path, null, body, responseType);
    }

    public void delete(String path) {
        request("DELETE", path, null, null, Void.class);
    }

    public <T> PagedResponse<T> getPaged(String path, Map<String, String> params, TypeReference<List<T>> itemType) {
        return requestPaged(path, params, itemType);
    }

    private <T> T request(String method, String path, Map<String, String> params, Object body, Class<T> responseType) {
        HttpResponse<String> response = fetchWithRetry(method, path, params, body);
        String responseBody = response.body();

        try {
            JavaType envelopeType = objectMapper.getTypeFactory()
                    .constructParametricType(ApiResponse.class, responseType);
            ApiResponse<T> apiResponse = objectMapper.readValue(responseBody, envelopeType);

            if (!apiResponse.isSuccess()) {
                throwApiError(apiResponse, response);
            }

            return apiResponse.getData();
        } catch (LivepassesException e) {
            throw e;
        } catch (Exception e) {
            // For void responses (e.g. delete), data may be null
            if (responseType == Void.class) {
                return null;
            }
            throw new LivepassesException(
                    "Failed to parse API response: " + e.getMessage(),
                    response.statusCode(), "PARSE_ERROR");
        }
    }

    private <T> PagedResponse<T> requestPaged(String path, Map<String, String> params, TypeReference<List<T>> itemType) {
        HttpResponse<String> response = fetchWithRetry("GET", path, params, null);
        String responseBody = response.body();

        try {
            JavaType listType = objectMapper.getTypeFactory().constructType(itemType);
            JavaType pagedType = objectMapper.getTypeFactory()
                    .constructParametricType(ApiPagedResponse.class, listType.getContentType());
            ApiPagedResponse<T> apiResponse = objectMapper.readValue(responseBody, pagedType);

            if (!apiResponse.isSuccess()) {
                ApiResponse.ApiError error = apiResponse.getError();
                String msg = error != null ? error.getMessage() : "API request failed with status " + response.statusCode();
                String code = error != null ? error.getCode() : "GENERAL_ERROR";
                String details = error != null ? error.getDetails() : null;
                throw ExceptionFactory.createTypedException(msg, response.statusCode(), code, details, parseRetryAfter(response));
            }

            return new PagedResponse<>(apiResponse.getItems(), apiResponse.getPagination());
        } catch (LivepassesException e) {
            throw e;
        } catch (Exception e) {
            throw new LivepassesException(
                    "Failed to parse paged API response: " + e.getMessage(),
                    response.statusCode(), "PARSE_ERROR");
        }
    }

    private HttpResponse<String> fetchWithRetry(String method, String path, Map<String, String> params, Object body) {
        String url = buildUrl(path, params);
        String bodyStr = null;
        if (body != null) {
            try {
                bodyStr = objectMapper.writeValueAsString(body);
            } catch (Exception e) {
                throw new LivepassesException("Failed to serialize request body: " + e.getMessage(), 0, "SERIALIZATION_ERROR");
            }
        }

        int maxAttempts = maxRetries + 1;
        Exception lastError = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(timeout)
                        .header("X-API-Key", apiKey)
                        .header("Accept", "application/json");

                if (bodyStr != null) {
                    reqBuilder.header("Content-Type", "application/json");
                    reqBuilder.method(method, HttpRequest.BodyPublishers.ofString(bodyStr));
                } else {
                    reqBuilder.method(method, HttpRequest.BodyPublishers.noBody());
                }

                HttpResponse<String> response = httpClient.send(reqBuilder.build(),
                        HttpResponse.BodyHandlers.ofString());

                // Retry on 429 (rate limit)
                if (response.statusCode() == 429 && attempt < maxAttempts) {
                    Integer retryAfter = parseRetryAfter(response);
                    long delay = retryAfter != null ? retryAfter * 1000L : getBackoffDelay(attempt);
                    sleep(delay);
                    continue;
                }

                // Retry on 5xx (server error) - fewer retries
                if (response.statusCode() >= 500 && attempt < Math.min(maxAttempts, 3)) {
                    sleep(getBackoffDelay(attempt));
                    continue;
                }

                return response;

            } catch (java.net.http.HttpTimeoutException e) {
                throw new LivepassesException(
                        "Request timed out after " + timeout.toMillis() + "ms",
                        0, "TIMEOUT");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new LivepassesException("Request interrupted", 0, "NETWORK_ERROR");
            } catch (IOException e) {
                lastError = e;
                if (attempt < maxAttempts) {
                    sleep(getBackoffDelay(attempt));
                    continue;
                }
            }
        }

        throw new LivepassesException(
                lastError != null ? lastError.getMessage() : "Request failed after retries",
                0, "NETWORK_ERROR");
    }

    private <T> void throwApiError(ApiResponse<T> apiResponse, HttpResponse<String> response) {
        ApiResponse.ApiError error = apiResponse.getError();
        String msg = error != null ? error.getMessage() : "API request failed with status " + response.statusCode();
        String code = error != null ? error.getCode() : "GENERAL_ERROR";
        String details = error != null ? error.getDetails() : null;
        throw ExceptionFactory.createTypedException(msg, response.statusCode(), code, details, parseRetryAfter(response));
    }

    private String buildUrl(String path, Map<String, String> params) {
        String cleanPath = path.startsWith("/") ? path : "/" + path;
        StringBuilder sb = new StringBuilder(baseUrl).append(cleanPath);

        if (params != null && !params.isEmpty()) {
            sb.append("?");
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    if (!first) sb.append("&");
                    sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                    sb.append("=");
                    sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                    first = false;
                }
            }
        }

        return sb.toString();
    }

    private static Integer parseRetryAfter(HttpResponse<String> response) {
        return response.headers().firstValue("Retry-After")
                .map(h -> {
                    try {
                        return Integer.parseInt(h);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    private static long getBackoffDelay(int attempt) {
        long base = Math.min(1000L * (1L << (attempt - 1)), 30000L);
        long jitter = ThreadLocalRandom.current().nextLong(500);
        return base + jitter;
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
