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

    @JsonProperty("pass.redeemed")
    PASS_REDEEMED,

    @JsonProperty("pass.updated")
    PASS_UPDATED,

    @JsonProperty("loyalty.transacted")
    LOYALTY_TRANSACTED,

    @JsonProperty("coupon.applied")
    COUPON_APPLIED,

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

    /** Every event above. */
    @JsonProperty("*")
    ALL
}
