package com.livepasses.sdk.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.livepasses.sdk.internal.LivepassesHttpClient;
import com.livepasses.sdk.internal.PaginationIterator;
import com.livepasses.sdk.internal.Polling;
import com.livepasses.sdk.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Operations on passes: generate, list, lookup, validate, redeem, check-in, etc.
 */
public class PassesResource {

    private final LivepassesHttpClient http;

    public PassesResource(LivepassesHttpClient http) {
        this.http = http;
    }

    /**
     * Generate passes for one or more recipients.
     * For a single recipient the pass is returned synchronously.
     * For multiple recipients, returns immediately with a batchId -- poll with getBatchStatus().
     */
    public PassGenerationResult generate(GeneratePassesParams params) {
        return http.post("/api/passes/generate", params, PassGenerationResult.class);
    }

    /**
     * Generate passes and automatically poll until the batch is complete.
     * For sync generation (1 recipient), returns immediately.
     * For async batches (>1 recipient), polls getBatchStatus() until done.
     */
    public PassGenerationResult generateAndWait(GeneratePassesParams params, GenerateAndWaitOptions options) {
        PassGenerationResult result = generate(params);

        if (!result.isAsyncProcessing()) {
            return result;
        }

        long interval = options != null ? options.getPollIntervalMs() : 2000L;
        int maxAttempts = options != null ? options.getMaxAttempts() : 150;
        java.util.function.Consumer<BatchStatusResult> onProgress =
                options != null ? options.getOnProgress() : null;

        String batchId = result.getBatchId();
        BatchStatusResult batchStatus = Polling.pollUntilComplete(
                () -> getBatchStatus(batchId),
                BatchStatusResult::isCompleted,
                interval,
                maxAttempts,
                onProgress
        );

        // Merge batch results into original result
        List<GeneratedPass> passes = new ArrayList<>();
        for (GeneratedPassSummary s : batchStatus.getGeneratedPasses()) {
            passes.add(new GeneratedPass(
                    s.getId(),
                    s.getHolderEmail(),
                    null,
                    new PassPlatforms(
                            new PassPlatform(s.isHasApplePass(), null, null, null, Collections.emptyList()),
                            new PassPlatform(s.isHasGooglePass(), null, null, null, Collections.emptyList())
                    ),
                    null,
                    null,
                    s.getStatus(),
                    null,
                    null,
                    null
            ));
        }
        result.setPasses(passes);
        return result;
    }

    /**
     * List passes (paginated).
     */
    public PagedResponse<GlobalPassDto> list(ListPassesParams params) {
        return http.getPaged(
                "/api/passes",
                params != null ? params.toQueryParams() : null,
                new TypeReference<List<GlobalPassDto>>() {
                });
    }

    /**
     * Auto-paginate through all passes matching the given filters.
     * Returns a lazy Iterator that fetches the next page only when needed.
     */
    public Iterator<GlobalPassDto> listAutoPaginate(ListPassesParams params) {
        ListPassesParams base = params != null ? params : ListPassesParams.builder().build();
        return new PaginationIterator<>(page -> list(base.withPage(page)));
    }

    /**
     * Look up a pass by ID or pass number.
     */
    public PassLookupResult lookup(LookupPassParams params) {
        return http.get("/api/passes/lookup", params.toQueryParams(), PassLookupResult.class);
    }

    /**
     * Validate a pass before redemption.
     */
    public PassValidationResult validate(String passId) {
        return http.get("/api/passes/" + passId + "/validate", null, PassValidationResult.class);
    }

    /**
     * Update a pass: change fields ({@code updatedFields}) and/or notify the holder
     * ({@code messageHeader}/{@code messageBody}). See {@link UpdatePassParams}.
     */
    public void update(String passId, UpdatePassParams params) {
        http.put("/api/passes/" + passId, params, Void.class);
    }

    /**
     * Push a scoped update to all eligible passes of a template.
     */
    public void pushTemplate(String templateId, PushTemplatePassesParams params) {
        http.post("/api/passes/template/" + templateId + "/push", params, Void.class);
    }

    /**
     * Redeem a single-use pass.
     *
     * <p>Redeeming is terminal, so a pass the holder is meant to keep using must not go through
     * here. Multi-use passes are refused with {@code 422} / {@code OPERATION_NOT_ALLOWED} instead
     * of being consumed. Use the operation built for the type:
     *
     * <ul>
     *   <li>loyalty and stamp cards: {@code stamp} (and {@code unstamp} to undo)
     *   <li>memberships: {@code membershipCheckIn}, which does not consume the pass
     *   <li>coupons that allow multiple redemptions: {@code redeemCoupon}
     *   <li>gift cards: {@code redeemGiftCard}, which takes the amount to deduct
     * </ul>
     */
    public PassRedemptionResult redeem(String passId, RedeemPassParams params) {
        return http.post("/api/passes/" + passId + "/redeem", params, PassRedemptionResult.class);
    }

    /**
     * Check in an event pass.
     */
    public PassRedemptionResult checkIn(String passId, CheckInParams params) {
        return http.post("/api/passes/" + passId + "/check-in", params, PassRedemptionResult.class);
    }

    /**
     * Redeem a coupon pass.
     */
    public PassRedemptionResult redeemCoupon(String passId, RedeemCouponParams params) {
        return http.post("/api/passes/" + passId + "/redeem-coupon", params, PassRedemptionResult.class);
    }

    /**
     * Earn or spend loyalty points.
     */
    public PassRedemptionResult loyaltyTransact(String passId, LoyaltyTransactionParams params) {
        return http.post("/api/passes/" + passId + "/loyalty/transact", params, PassRedemptionResult.class);
    }

    /**
     * Deduct an amount from a gift card's balance. A redemption for more than the remaining
     * balance is rejected, and the balance is left untouched in that case.
     */
    public PassRedemptionResult redeemGiftCard(String passId, RedeemGiftCardParams params) {
        return http.post("/api/passes/" + passId + "/giftcard/redeem", params, PassRedemptionResult.class);
    }

    /**
     * Check in a membership pass. Unlike an event check-in the pass is NOT consumed — it stays
     * valid for the next visit. On a quota-limited membership the remaining uses decrement, and a
     * check-in at zero is denied.
     */
    public PassRedemptionResult membershipCheckIn(String passId, MembershipCheckInParams params) {
        return http.post("/api/passes/" + passId + "/membership/check-in", params, PassRedemptionResult.class);
    }

    /**
     * Add one stamp to the stamp card behind this pass. Repeat stamps on the same card are refused
     * inside a short cooldown, so a double scan at the till does not award two stamps.
     */
    public PassRedemptionResult stamp(String passId) {
        // Passes an empty map, not null: the endpoint binds a request DTO, and a null body is sent
        // as noBody() with no Content-Type, which FastEndpoints answers with 415.
        return http.post("/api/passes/" + passId + "/stamp", Map.of(), PassRedemptionResult.class);
    }

    /**
     * Take back the most recent stamp, for correcting a mis-scan. Refused when there is nothing to
     * undo, or when the last stamp was paid for by an external order.
     */
    public PassRedemptionResult unstamp(String passId) {
        // See stamp(): an empty map rather than null, or the request-DTO endpoint answers 415.
        return http.post("/api/passes/" + passId + "/unstamp", Map.of(), PassRedemptionResult.class);
    }

    /**
     * Resolve a scanned barcode or NFC tap value and redeem it in one call.
     */
    public PassRedemptionResult redeemByScan(RedeemByScanParams params) {
        return http.post("/api/passes/redeem-by-scan", params, PassRedemptionResult.class);
    }

    /**
     * Get the status of a batch pass generation operation.
     */
    public BatchStatusResult getBatchStatus(String batchId) {
        return http.get("/api/passes/batch/" + batchId + "/status", null, BatchStatusResult.class);
    }
}
