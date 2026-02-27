package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WebhookEventType {

    @JsonProperty("pass.generated")
    PASS_GENERATED,

    @JsonProperty("pass.redeemed")
    PASS_REDEEMED,

    @JsonProperty("pass.updated")
    PASS_UPDATED,

    @JsonProperty("pass.expired")
    PASS_EXPIRED,

    @JsonProperty("pass.checked_in")
    PASS_CHECKED_IN,

    @JsonProperty("batch.completed")
    BATCH_COMPLETED,

    @JsonProperty("batch.failed")
    BATCH_FAILED
}
