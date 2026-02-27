package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoyaltyTransactionParams {

    private final String transactionType;
    private final int points;
    private final String description;

    private LoyaltyTransactionParams(Builder builder) {
        this.transactionType = builder.transactionType;
        this.points = builder.points;
        this.description = builder.description;
    }

    public static Builder builder() { return new Builder(); }

    public String getTransactionType() { return transactionType; }
    public int getPoints() { return points; }
    public String getDescription() { return description; }

    public static class Builder {
        private String transactionType;
        private int points;
        private String description;

        /** Transaction type: "earn" or "spend". */
        public Builder transactionType(String v) { this.transactionType = v; return this; }
        public Builder points(int v) { this.points = v; return this; }
        public Builder description(String v) { this.description = v; return this; }

        public LoyaltyTransactionParams build() {
            if (transactionType == null) throw new IllegalArgumentException("transactionType is required");
            return new LoyaltyTransactionParams(this);
        }
    }
}
