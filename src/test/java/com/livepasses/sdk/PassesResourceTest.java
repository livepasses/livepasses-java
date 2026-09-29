package com.livepasses.sdk;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.livepasses.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@WireMockTest
class PassesResourceTest {

    private Livepasses createClient(WireMockRuntimeInfo wm) {
        return new Livepasses("lp_test_key",
                LivepassesOptions.builder()
                        .baseUrl(wm.getHttpBaseUrl())
                        .timeout(Duration.ofSeconds(5))
                        .maxRetries(0)
                        .build());
    }

    @Test
    void shouldGenerateSinglePass(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(false)))));

        Livepasses client = createClient(wm);
        PassGenerationResult result = client.passes().generate(
                GeneratePassesParams.builder()
                        .templateId("tpl-1")
                        .passes(List.of(PassRecipient.builder()
                                .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                                .businessData(BusinessData.builder().sectionInfo("VIP").rowInfo("A").seatNumber("1").build())
                                .build()))
                        .build());

        assertThat(result.isAsyncProcessing()).isFalse();
        assertThat(result.getTotalPasses()).isEqualTo(1);
        assertThat(result.getPasses()).hasSize(1);
        assertThat(result.getPasses().get(0).getId()).isEqualTo("pass-1");
        assertThat(result.getPasses().get(0).getPlatforms().getApple().isAvailable()).isTrue();
        assertThat(result.getPasses().get(0).getPlatforms().getGoogle().isAvailable()).isTrue();
    }

    @Test
    void generatedPassKeepsItsPreErrorFieldsConstructor() {
        // Binary compatibility: callers compiled against the constructor from before errorCode and
        // errorMessage were added must keep linking. The overload leaves both null.
        GeneratedPass pass = new GeneratedPass(
                "pass-1", "jane@example.com", null, null, null, null, "active", null);

        assertThat(pass.getId()).isEqualTo("pass-1");
        assertThat(pass.getStatus()).isEqualTo("active");
        assertThat(pass.getErrorCode()).isNull();
        assertThat(pass.getErrorMessage()).isNull();
    }

    @Test
    void shouldGenerateAndWaitSync(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(false)))));

        Livepasses client = createClient(wm);
        PassGenerationResult result = client.passes().generateAndWait(
                GeneratePassesParams.builder()
                        .templateId("tpl-1")
                        .passes(List.of(PassRecipient.builder()
                                .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                                .businessData(BusinessData.builder().sectionInfo("VIP").build())
                                .build()))
                        .build(),
                null);

        assertThat(result.isAsyncProcessing()).isFalse();
        assertThat(result.getPasses()).hasSize(1);
        // Should not have called getBatchStatus
        verify(0, getRequestedFor(urlPathMatching("/api/passes/batch/.*")));
    }

    @Test
    void shouldGenerateAndWaitAsyncWithPolling(WireMockRuntimeInfo wm) {
        // Step 1: generate returns async
        stubFor(post(urlEqualTo("/api/passes/generate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passGenerationResult(true)))));

        // Step 2: first poll - processing
        stubFor(get(urlEqualTo("/api/passes/batch/batch-123/status"))
                .inScenario("polling")
                .whenScenarioStateIs("Started")
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.batchStatusResult(false, 40.0))))
                .willSetStateTo("poll-2"));

        // Step 3: second poll - completed
        stubFor(get(urlEqualTo("/api/passes/batch/batch-123/status"))
                .inScenario("polling")
                .whenScenarioStateIs("poll-2")
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.batchStatusResult(true, 100.0)))));

        Livepasses client = createClient(wm);
        PassGenerationResult result = client.passes().generateAndWait(
                GeneratePassesParams.builder()
                        .templateId("tpl-1")
                        .passes(List.of(
                                PassRecipient.builder()
                                        .customer(CustomerInfo.builder().firstName("A").lastName("B").build())
                                        .businessData(BusinessData.builder().build())
                                        .build(),
                                PassRecipient.builder()
                                        .customer(CustomerInfo.builder().firstName("C").lastName("D").build())
                                        .businessData(BusinessData.builder().build())
                                        .build()
                        ))
                        .build(),
                GenerateAndWaitOptions.builder()
                        .pollIntervalMs(10)  // fast polling for test
                        .maxAttempts(10)
                        .build());

        assertThat(result.getPasses()).hasSize(1);
        assertThat(result.getPasses().get(0).getId()).isEqualTo("p-1");
        verify(1, postRequestedFor(urlEqualTo("/api/passes/generate")));
        verify(2, getRequestedFor(urlEqualTo("/api/passes/batch/batch-123/status")));
    }

    @Test
    void shouldListPasses(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes"))
                .willReturn(okJson(MockResponses.pagedEnvelope(
                        "[" + MockResponses.globalPassDto() + "]", 1, 20, 1, 1))));

        Livepasses client = createClient(wm);
        PagedResponse<GlobalPassDto> result = client.passes().list(
                ListPassesParams.builder().page(1).pageSize(20).build());

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getId()).isEqualTo("pass-1");
    }

    @Test
    void shouldLookupPass(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes/lookup"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passLookupResult()))));

        Livepasses client = createClient(wm);
        PassLookupResult result = client.passes().lookup(
                LookupPassParams.builder().passId("pass-1").build());

        assertThat(result.getPassId()).isEqualTo("pass-1");
        assertThat(result.isValid()).isTrue();
        assertThat(result.isCanBeRedeemed()).isTrue();
    }

    @Test
    void shouldValidatePass(WireMockRuntimeInfo wm) {
        stubFor(get(urlPathEqualTo("/api/passes/pass-1/validate"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passValidationResult()))));

        Livepasses client = createClient(wm);
        PassValidationResult result = client.passes().validate("pass-1");

        assertThat(result.isCanBeRedeemed()).isTrue();
        assertThat(result.getVerificationMethods()).containsExactly("qr_code", "manual");
    }

    @Test
    void shouldRedeemPass(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/pass-1/redeem"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passRedemptionResult()))));

        Livepasses client = createClient(wm);
        PassRedemptionResult result = client.passes().redeem("pass-1", null);

        assertThat(result.getNewStatus()).isEqualTo("redeemed");
        assertThat(result.isAlreadyRedeemed()).isFalse();
    }

    @Test
    void shouldCheckInWithLocation(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/pass-1/check-in"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passRedemptionResult()))));

        Livepasses client = createClient(wm);
        PassRedemptionResult result = client.passes().checkIn("pass-1",
                CheckInParams.builder()
                        .location(RedemptionLocation.builder()
                                .name("Gate 1").latitude(40.71).longitude(-74.00).build())
                        .build());

        assertThat(result.getNewStatus()).isEqualTo("redeemed");

        verify(postRequestedFor(urlEqualTo("/api/passes/pass-1/check-in"))
                .withRequestBody(containing("\"latitude\":40.71")));
    }

    @Test
    void updateSendsOnlyTheFieldsThePassUpdateEndpointDeclares(WireMockRuntimeInfo wm) {
        // The API refuses any undeclared body field with a 400 (#797). The body must be exactly
        // UpdatePassCommand's shape: updatedFields, reason, messageHeader, messageBody, notify.
        stubFor(put(urlEqualTo("/api/passes/pass-1"))
                .willReturn(okJson(MockResponses.successEnvelope("null"))));

        Livepasses client = createClient(wm);
        client.passes().update("pass-1",
                UpdatePassParams.builder()
                        .updatedField("memberTier", "Gold")
                        .updatedField("points", 600)
                        .reason("Tier upgrade")
                        .messageHeader("Welcome to Gold")
                        .messageBody("Enjoy double points this month!")
                        .notify(true)
                        .build());

        verify(putRequestedFor(urlEqualTo("/api/passes/pass-1"))
                .withRequestBody(equalToJson("{"
                        + "\"updatedFields\":{\"memberTier\":\"Gold\",\"points\":600},"
                        + "\"reason\":\"Tier upgrade\","
                        + "\"messageHeader\":\"Welcome to Gold\","
                        + "\"messageBody\":\"Enjoy double points this month!\","
                        + "\"notify\":true}")));
    }

    @Test
    void updateOmitsUnsetFields(WireMockRuntimeInfo wm) {
        stubFor(put(urlEqualTo("/api/passes/pass-1"))
                .willReturn(okJson(MockResponses.successEnvelope("null"))));

        Livepasses client = createClient(wm);
        client.passes().update("pass-1",
                UpdatePassParams.builder()
                        .updatedFields(java.util.Map.of("validUntil", "2026-12-31"))
                        .build());

        verify(putRequestedFor(urlEqualTo("/api/passes/pass-1"))
                .withRequestBody(equalToJson("{\"updatedFields\":{\"validUntil\":\"2026-12-31\"}}")));
    }

    @Test
    void redeemFamilyBodiesCarryOnlyDeclaredFields(WireMockRuntimeInfo wm) {
        // notes was never declared by any redeem endpoint; the API now refuses it (#797).
        stubFor(post(urlMatching("/api/passes/pass-1/(redeem|check-in|redeem-coupon)"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passRedemptionResult()))));

        Livepasses client = createClient(wm);
        RedemptionLocation location = RedemptionLocation.builder().name("Store #42").build();
        java.util.Map<String, String> metadata = java.util.Map.of("orderId", "12345");

        client.passes().redeem("pass-1",
                RedeemPassParams.builder().location(location).metadata(metadata).build());
        client.passes().checkIn("pass-1",
                CheckInParams.builder().location(location).metadata(metadata).build());
        client.passes().redeemCoupon("pass-1",
                RedeemCouponParams.builder().location(location).metadata(metadata).build());

        String expected = "{\"location\":{\"name\":\"Store #42\"},\"metadata\":{\"orderId\":\"12345\"}}";
        for (String path : List.of("redeem", "check-in", "redeem-coupon")) {
            verify(postRequestedFor(urlEqualTo("/api/passes/pass-1/" + path))
                    .withRequestBody(equalToJson(expected)));
        }
    }

    @Test
    void shouldEarnLoyaltyPoints(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/api/passes/pass-1/loyalty/transact"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.passRedemptionResult()))));

        Livepasses client = createClient(wm);
        PassRedemptionResult result = client.passes().loyaltyTransact("pass-1",
                LoyaltyTransactionParams.builder()
                        .transactionType("earn").points(100).description("Purchase reward").build());

        assertThat(result).isNotNull();

        verify(postRequestedFor(urlEqualTo("/api/passes/pass-1/loyalty/transact"))
                .withRequestBody(containing("\"transactionType\":\"earn\""))
                .withRequestBody(containing("\"points\":100")));
    }

    @Test
    void shouldGetBatchStatus(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/api/passes/batch/batch-123/status"))
                .willReturn(okJson(MockResponses.successEnvelope(
                        MockResponses.batchStatusResult(true, 100.0)))));

        Livepasses client = createClient(wm);
        BatchStatusResult result = client.passes().getBatchStatus("batch-123");

        assertThat(result.isCompleted()).isTrue();
        assertThat(result.getProgressPercentage()).isEqualTo(100.0);
    }
}
