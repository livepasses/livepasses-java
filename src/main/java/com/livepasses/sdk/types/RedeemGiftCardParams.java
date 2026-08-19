package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Deduct an amount from a gift card's balance.
 *
 * <p>A redemption for more than the remaining balance is rejected, and the balance is left
 * untouched in that case.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedeemGiftCardParams {

    private final Double amount;
    private final String reason;
    private final String redemptionChannel;
    private final RedemptionLocation location;

    private RedeemGiftCardParams(Builder builder) {
        this.amount = builder.amount;
        this.reason = builder.reason;
        this.redemptionChannel = builder.redemptionChannel;
        this.location = builder.location;
    }

    public static Builder builder() { return new Builder(); }

    public Double getAmount() { return amount; }
    public String getReason() { return reason; }
    public String getRedemptionChannel() { return redemptionChannel; }
    public RedemptionLocation getLocation() { return location; }

    public static class Builder {
        private Double amount;
        private String reason;
        private String redemptionChannel;
        private RedemptionLocation location;

        public Builder amount(Double v) { this.amount = v; return this; }
        public Builder reason(String v) { this.reason = v; return this; }
        public Builder redemptionChannel(String v) { this.redemptionChannel = v; return this; }
        public Builder location(RedemptionLocation v) { this.location = v; return this; }

        public RedeemGiftCardParams build() { return new RedeemGiftCardParams(this); }
    }
}
