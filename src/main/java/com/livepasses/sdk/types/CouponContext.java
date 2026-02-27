package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponContext {

    private final String campaignName;
    private final String specialMessage;
    private final String promotionStartDate;
    private final String promotionEndDate;

    private CouponContext(Builder builder) {
        this.campaignName = builder.campaignName;
        this.specialMessage = builder.specialMessage;
        this.promotionStartDate = builder.promotionStartDate;
        this.promotionEndDate = builder.promotionEndDate;
    }

    public static Builder builder() { return new Builder(); }

    public String getCampaignName() { return campaignName; }
    public String getSpecialMessage() { return specialMessage; }
    public String getPromotionStartDate() { return promotionStartDate; }
    public String getPromotionEndDate() { return promotionEndDate; }

    public static class Builder {
        private String campaignName;
        private String specialMessage;
        private String promotionStartDate;
        private String promotionEndDate;

        public Builder campaignName(String v) { this.campaignName = v; return this; }
        public Builder specialMessage(String v) { this.specialMessage = v; return this; }
        public Builder promotionStartDate(String v) { this.promotionStartDate = v; return this; }
        public Builder promotionEndDate(String v) { this.promotionEndDate = v; return this; }

        public CouponContext build() { return new CouponContext(this); }
    }
}
