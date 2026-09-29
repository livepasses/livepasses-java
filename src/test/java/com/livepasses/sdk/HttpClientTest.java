package com.livepasses.sdk;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.internal.LivepassesHttpClient;
import com.livepasses.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@WireMockTest
class HttpClientTest {

    private Livepasses createClient(WireMockRuntimeInfo wm) {
        return new Livepasses("lp_test_key_123",
                LivepassesOptions.builder()
                        .baseUrl(wm.getHttpBaseUrl())
                        .timeout(Duration.ofSeconds(5))
                        .maxRetries(0)
                        .build());
    }

    private LivepassesHttpClient client(WireMockRuntimeInfo wm, int retries) {
        return new LivepassesHttpClient("lp_test_key_123", wm.getHttpBaseUrl(), Duration.ofSeconds(5), retries);
    }

    @Test
    void shouldSendApiKeyHeader(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(false)))));

        Livepasses client = createClient(wm);
        client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                        .businessData(BusinessData.builder().sectionInfo("A").build())
                        .build()))
                .build());

        verify(postRequestedFor(urlEqualTo("/api/passes/generate"))
                .withHeader("X-API-Key", equalTo("lp_test_key_123")));
    }

    @Test
    void shouldSendPostWithJsonBody(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(false)))));

        Livepasses client = createClient(wm);
        client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                        .businessData(BusinessData.builder().sectionInfo("A").build())
                        .build()))
                .build());

        verify(postRequestedFor(urlEqualTo("/api/passes/generate"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"templateId\":\"tpl-1\"")));
    }

    @Test
    void shouldSerializeQueryParams(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes"))
                .willReturn(okJson(MockResponses.pagedEnvelope(
                        "[" + MockResponses.globalPassDto() + "]", 1, 20, 1, 1))));

        Livepasses client = createClient(wm);
        client.passes().list(ListPassesParams.builder().page(1).pageSize(20).build());

        verify(getRequestedFor(urlPathEqualTo("/api/passes"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("pageSize", equalTo("20")));
    }

    @Test
    void shouldUnwrapApiResponse(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes/pass-1/validate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passValidationResult()))));

        Livepasses client = createClient(wm);
        PassValidationResult result = client.passes().validate("pass-1");

        assertThat(result).isNotNull();
        assertThat(result.isCanBeRedeemed()).isTrue();
        assertThat(result.getPassId()).isEqualTo("pass-1");
    }

    @Test
    void shouldThrowAuthenticationException(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody(MockResponses.errorEnvelope("UNAUTHORIZED", "Invalid API key", null))));

        Livepasses client = createClient(wm);

        assertThatThrownBy(() -> client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("J").lastName("D").build())
                        .businessData(BusinessData.builder().build())
                        .build()))
                .build()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Invalid API key");
    }

    @Test
    void shouldThrowNotFoundException(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes/pass-999/validate"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(MockResponses.errorEnvelope("NOT_FOUND", "Pass not found", null))));

        Livepasses client = createClient(wm);

        assertThatThrownBy(() -> client.passes().validate("pass-999"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowValidationException(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(MockResponses.errorEnvelope("VALIDATION_ERROR",
                                "Invalid input", "templateId is required"))));

        Livepasses client = createClient(wm);

        assertThatThrownBy(() -> client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("J").lastName("D").build())
                        .businessData(BusinessData.builder().build())
                        .build()))
                .build()))
                .isInstanceOf(ValidationException.class)
                .satisfies(e -> {
                    ValidationException ve = (ValidationException) e;
                    assertThat(ve.getDetails()).isEqualTo("templateId is required");
                });
    }

    @Test
    void shouldThrowRateLimitExceptionWithRetryAfter(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Retry-After", "30")
                        .withBody(MockResponses.errorEnvelope("RATE_LIMIT_EXCEEDED",
                                "Too many requests", null))));

        Livepasses client = createClient(wm);

        assertThatThrownBy(() -> client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("J").lastName("D").build())
                        .businessData(BusinessData.builder().build())
                        .build()))
                .build()))
                .isInstanceOf(RateLimitException.class)
                .satisfies(e -> {
                    RateLimitException rle = (RateLimitException) e;
                    assertThat(rle.getRetryAfter()).isEqualTo(30);
                });
    }

    @Test
    void shouldRetryOn5xxAndSucceed(WireMockRuntimeInfo wm) {
        // GET is idempotent, so a 5xx is retried. First request returns 500, second returns success.
        stubFor(get(urlPathEqualTo("/api/passes/pass-1/validate"))
                .inScenario("retry-test")
                .whenScenarioStateIs("Started")
                .willReturn(aResponse().withStatus(500).withBody("{\"success\":false}"))
                .willSetStateTo("retried"));

        stubFor(get(urlPathEqualTo("/api/passes/pass-1/validate"))
                .inScenario("retry-test")
                .whenScenarioStateIs("retried")
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passValidationResult()))));

        Livepasses client = new Livepasses("lp_test_key_123",
                LivepassesOptions.builder()
                        .baseUrl(wm.getHttpBaseUrl())
                        .timeout(Duration.ofSeconds(5))
                        .maxRetries(2)
                        .build());

        PassValidationResult result = client.passes().validate("pass-1");

        assertThat(result).isNotNull();
        assertThat(result.getPassId()).isEqualTo("pass-1");
        verify(2, getRequestedFor(urlPathEqualTo("/api/passes/pass-1/validate")));
    }

    @Test
    void shouldParsePagedResponse(WireMockRuntimeInfo wm) {
        String items = "[" + MockResponses.globalPassDto() + "]";
        stubFor(get(urlPathEqualTo("/api/passes"))
                .willReturn(okJson(MockResponses.pagedEnvelope(items, 1, 20, 3, 55))));

        Livepasses client = createClient(wm);
        PagedResponse<GlobalPassDto> result = client.passes().list(null);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getPagination().getCurrentPage()).isEqualTo(1);
        assertThat(result.getPagination().getTotalPages()).isEqualTo(3);
        assertThat(result.getPagination().getTotalItems()).isEqualTo(55);
    }

    @Test
    void refusalWithStatusRaisesTypedErrorFromEnvelope(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/api/templates/x")).willReturn(aResponse().withStatus(404)
            .withBody("{\"success\":false,\"data\":null,\"error\":{\"code\":\"TEMPLATE_NOT_FOUND\",\"message\":\"gone\"}}")));
        assertThatThrownBy(() -> client(wm, 0).get("/api/templates/x", null, Object.class))
            .isInstanceOf(NotFoundException.class).extracting("code").isEqualTo("TEMPLATE_NOT_FOUND");
    }

    @Test
    void validationErrorCarriesFields(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/x")).willReturn(aResponse().withStatus(400)
            .withBody("{\"success\":false,\"error\":{\"code\":\"VALIDATION_ERROR\",\"message\":\"bad\",\"fields\":{\"name\":[\"required\"]}}}")));
        assertThatThrownBy(() -> client(wm, 0).post("/api/x", Map.of(), Object.class))
            .isInstanceOfSatisfying(ValidationException.class, e -> assertThat(e.getFields()).containsEntry("name", List.of("required")));
    }

    @Test
    void forbiddenStatusWinsOverAnUnauthorizedCode(WireMockRuntimeInfo wm) {
        // A handler-level UNAUTHORIZED refusal answers 403: a permission problem, not a bad key.
        stubFor(get(urlEqualTo("/api/x")).willReturn(aResponse().withStatus(403)
            .withBody("{\"success\":false,\"data\":null,\"error\":{\"code\":\"UNAUTHORIZED\",\"message\":\"no\"}}")));
        assertThatThrownBy(() -> client(wm, 0).get("/api/x", null, Object.class))
            .isInstanceOfSatisfying(ForbiddenException.class, e -> {
                assertThat(e.getStatus()).isEqualTo(403);
                assertThat(e.getCode()).isEqualTo("UNAUTHORIZED");
            });
    }

    @Test
    void quotaExceededCarriesTheRealStatus(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/templates")).willReturn(aResponse().withStatus(422)
            .withBody("{\"success\":false,\"data\":null,\"error\":{\"code\":\"QUOTA_EXCEEDED\",\"message\":\"limit\"}}")));
        assertThatThrownBy(() -> client(wm, 0).post("/api/templates", Map.of(), Object.class))
            .isInstanceOfSatisfying(QuotaExceededException.class, e -> assertThat(e.getStatus()).isEqualTo(422));
    }

    @Test
    void emptyBody401IsAuthenticationException(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/api/x")).willReturn(aResponse().withStatus(401)));
        assertThatThrownBy(() -> client(wm, 0).get("/api/x", null, Object.class))
            .isInstanceOfSatisfying(AuthenticationException.class, e -> assertThat(e.getStatus()).isEqualTo(401));
    }

    @Test
    void typedExceptionConstructorsKeepTheirOldDefaultStatus() {
        assertThat(new QuotaExceededException("m", "QUOTA_EXCEEDED", null).getStatus()).isEqualTo(403);
        assertThat(new ForbiddenException("m", "FORBIDDEN", null, 451).getStatus()).isEqualTo(451);
    }

    @Test
    void apiErrorKeepsItsThreeArgumentConstructor() {
        ApiResponse.ApiError error = new ApiResponse.ApiError("gone", "NOT_FOUND", null);
        assertThat(error.getCode()).isEqualTo("NOT_FOUND");
        assertThat(error.getFields()).isNull();
    }

    @Test
    void emptyBodyErrorRaisesTypedError(WireMockRuntimeInfo wm) {
        stubFor(delete(urlEqualTo("/api/webhooks/x")).willReturn(aResponse().withStatus(404)));
        assertThatThrownBy(() -> client(wm, 0).delete("/api/webhooks/x")).isInstanceOf(NotFoundException.class);
    }

    @Test
    void retriesServerErrorOnlyForIdempotentMethods(WireMockRuntimeInfo wm) {
        String body = "{\"success\":false,\"error\":{\"code\":\"EXTERNAL_SERVICE_ERROR\",\"message\":\"upstream\"}}";
        stubFor(any(anyUrl()).willReturn(aResponse().withStatus(502).withBody(body)));
        assertThatThrownBy(() -> client(wm, 3).post("/api/passes/generate", Map.of(), Object.class)).isInstanceOf(LivepassesException.class);
        verify(1, postRequestedFor(urlEqualTo("/api/passes/generate")));
        assertThatThrownBy(() -> client(wm, 3).get("/api/passes/x", null, Object.class)).isInstanceOf(LivepassesException.class);
        verify(3, getRequestedFor(urlEqualTo("/api/passes/x")));
    }

    @Test
    void pagedRefusalWithNonJsonBodyRaisesTypedError(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes")).willReturn(aResponse().withStatus(502).withBody("<html>Bad Gateway</html>")));

        assertThatThrownBy(() -> createClient(wm).passes().list(null)).isInstanceOf(LivepassesException.class);
    }
}
