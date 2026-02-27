package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessContext {

    private final EventContext event;
    private final LoyaltyContext loyalty;
    private final CouponContext coupon;

    private BusinessContext(Builder builder) {
        this.event = builder.event;
        this.loyalty = builder.loyalty;
        this.coupon = builder.coupon;
    }

    public static Builder builder() { return new Builder(); }

    public EventContext getEvent() { return event; }
    public LoyaltyContext getLoyalty() { return loyalty; }
    public CouponContext getCoupon() { return coupon; }

    public static class Builder {
        private EventContext event;
        private LoyaltyContext loyalty;
        private CouponContext coupon;

        public Builder event(EventContext v) { this.event = v; return this; }
        public Builder loyalty(LoyaltyContext v) { this.loyalty = v; return this; }
        public Builder coupon(CouponContext v) { this.coupon = v; return this; }

        public BusinessContext build() { return new BusinessContext(this); }
    }
}
