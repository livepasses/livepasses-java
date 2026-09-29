package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Events the API accepts on a webhook subscription.
 *
 * <p>This enum mirrors the server's allow-list exactly. Subscribing to anything outside it is
 * rejected with a 400, so a value that is not here is not a "not yet supported" event — it is a
 * request that always fails.
 */
public enum WebhookEventType {

    @JsonProperty("pass.generated")
    PASS_GENERATED,

    /**
     * The holder saved the pass to a wallet: it went from no device to one. A second device does
     * not fire it again; a re-add after PASS_REMOVED does.
     */
    @JsonProperty("pass.installed")
    PASS_INSTALLED,

    /**
     * The holder removed the pass from their wallet and it is on no device. Holder-initiated only:
     * cancellation, transfer and operator ejection never fire it.
     */
    @JsonProperty("pass.removed")
    PASS_REMOVED,

    @JsonProperty("pass.redeemed")
    PASS_REDEEMED,

    @JsonProperty("pass.updated")
    PASS_UPDATED,

    @JsonProperty("pass.cancelled")
    PASS_CANCELLED,

    @JsonProperty("pass.expired")
    PASS_EXPIRED,

    @JsonProperty("loyalty.transacted")
    LOYALTY_TRANSACTED,

    @JsonProperty("coupon.applied")
    COUPON_APPLIED,

    /**
     * A membership pass was scanned at a door. Distinct from PASS_REDEEMED, which for a
     * single-use pass means the entitlement is now spent.
     */
    @JsonProperty("membership.checked_in")
    MEMBERSHIP_CHECKED_IN,

    @JsonProperty("transfer.initiated")
    TRANSFER_INITIATED,

    @JsonProperty("transfer.accepted")
    TRANSFER_ACCEPTED,

    @JsonProperty("transfer.declined")
    TRANSFER_DECLINED,

    @JsonProperty("transfer.revoked")
    TRANSFER_REVOKED,

    @JsonProperty("transfer.expired")
    TRANSFER_EXPIRED,

    /**
     * Advisory: raised when membership sharing detection flags a pass, e.g. the same card
     * checking in at too many distinct venues within a window. The triggering check-in still
     * succeeded; this event never blocks or denies anything.
     */
    @JsonProperty("pass.sharing_suspected")
    PASS_SHARING_SUSPECTED,

    /** Every event above. */
    @JsonProperty("*")
    ALL
}
