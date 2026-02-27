package com.livepasses.sdk;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

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
        // First request returns 500, second returns success
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .inScenario("retry-test")
                .whenScenarioStateIs("Started")
                .willReturn(aResponse().withStatus(500).withBody("{\"success\":false}"))
                .willSetStateTo("retried"));

        stubFor(post(urlEqualTo("/api/passes/generate"))
                .inScenario("retry-test")
                .whenScenarioStateIs("retried")
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(false)))));

        Livepasses client = new Livepasses("lp_test_key_123",
                LivepassesOptions.builder()
                        .baseUrl(wm.getHttpBaseUrl())
                        .timeout(Duration.ofSeconds(5))
                        .maxRetries(2)
                        .build());

        PassGenerationResult result = client.passes().generate(GeneratePassesParams.builder()
                .templateId("tpl-1")
                .passes(List.of(PassRecipient.builder()
                        .customer(CustomerInfo.builder().firstName("J").lastName("D").build())
                        .businessData(BusinessData.builder().build())
                        .build()))
                .build());

        assertThat(result).isNotNull();
        assertThat(result.getPasses()).hasSize(1);
        verify(2, postRequestedFor(urlEqualTo("/api/passes/generate")));
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
}
