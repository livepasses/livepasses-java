package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Standard API response envelope.
 * All Livepasses API endpoints return this structure.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;
    private final String message;

    public ApiResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("data") T data,
            @JsonProperty("error") ApiError error,
            @JsonProperty("message") String message) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiError {
        private final String message;
        private final String code;
        private final String details;
        private final Map<String, List<String>> fields;

        /** Creates an error with no field-level failures. Kept for source compatibility. */
        public ApiError(String message, String code, String details) {
            this(message, code, details, null);
        }

        @JsonCreator
        public ApiError(
                @JsonProperty("message") String message,
                @JsonProperty("code") String code,
                @JsonProperty("details") String details,
                @JsonProperty("fields") Map<String, List<String>> fields) {
            this.message = message;
            this.code = code;
            this.details = details;
            this.fields = fields;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public String getDetails() {
            return details;
        }

        /** Field path (camelCase, e.g. operations[0].path) -> validation messages. Present only for VALIDATION_ERROR. */
        public Map<String, List<String>> getFields() {
            return fields;
        }
    }
}
