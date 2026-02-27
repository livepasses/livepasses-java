package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoyaltyContext {

    private final String programUpdate;
    private final String seasonalMessage;

    private LoyaltyContext(Builder builder) {
        this.programUpdate = builder.programUpdate;
        this.seasonalMessage = builder.seasonalMessage;
    }

    public static Builder builder() { return new Builder(); }

    public String getProgramUpdate() { return programUpdate; }
    public String getSeasonalMessage() { return seasonalMessage; }

    public static class Builder {
        private String programUpdate;
        private String seasonalMessage;

        public Builder programUpdate(String v) { this.programUpdate = v; return this; }
        public Builder seasonalMessage(String v) { this.seasonalMessage = v; return this; }

        public LoyaltyContext build() { return new LoyaltyContext(this); }
    }
}
