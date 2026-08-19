package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Check in a membership pass.
 *
 * <p>Unlike an event check-in the pass is NOT consumed — it stays valid for the next visit. On a
 * quota-limited membership the remaining uses decrement, and a check-in at zero is denied.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipCheckInParams {

    private final String gate;
    private final String redemptionMethod;
    private final RedemptionLocation location;

    private MembershipCheckInParams(Builder builder) {
        this.gate = builder.gate;
        this.redemptionMethod = builder.redemptionMethod;
        this.location = builder.location;
    }

    public static Builder builder() { return new Builder(); }

    public String getGate() { return gate; }
    public String getRedemptionMethod() { return redemptionMethod; }
    public RedemptionLocation getLocation() { return location; }

    public static class Builder {
        private String gate;
        private String redemptionMethod;
        private RedemptionLocation location;

        public Builder gate(String v) { this.gate = v; return this; }
        public Builder redemptionMethod(String v) { this.redemptionMethod = v; return this; }
        public Builder location(RedemptionLocation v) { this.location = v; return this; }

        public MembershipCheckInParams build() { return new MembershipCheckInParams(this); }
    }
}
