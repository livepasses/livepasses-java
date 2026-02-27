package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessData {

    // Event-specific
    private final String sectionInfo;
    private final String rowInfo;
    private final String seatNumber;
    private final String gateInfo;
    private final String confirmationCode;
    private final String ticketType;
    private final Double price;
    private final String currency;

    // Loyalty-specific
    private final String membershipNumber;
    private final Integer currentPoints;
    private final String memberTier;
    private final Integer lifetimePoints;
    private final Double accountBalance;
    private final String memberSince;
    private final String favoriteStore;

    // Coupon-specific
    private final String promoCode;
    private final String campaignId;
    private final String customerSegment;
    private final Integer maxUsageCount;
    private final String sourceChannel;

    private BusinessData(Builder builder) {
        this.sectionInfo = builder.sectionInfo;
        this.rowInfo = builder.rowInfo;
        this.seatNumber = builder.seatNumber;
        this.gateInfo = builder.gateInfo;
        this.confirmationCode = builder.confirmationCode;
        this.ticketType = builder.ticketType;
        this.price = builder.price;
        this.currency = builder.currency;
        this.membershipNumber = builder.membershipNumber;
        this.currentPoints = builder.currentPoints;
        this.memberTier = builder.memberTier;
        this.lifetimePoints = builder.lifetimePoints;
        this.accountBalance = builder.accountBalance;
        this.memberSince = builder.memberSince;
        this.favoriteStore = builder.favoriteStore;
        this.promoCode = builder.promoCode;
        this.campaignId = builder.campaignId;
        this.customerSegment = builder.customerSegment;
        this.maxUsageCount = builder.maxUsageCount;
        this.sourceChannel = builder.sourceChannel;
    }

    public static Builder builder() { return new Builder(); }

    public String getSectionInfo() { return sectionInfo; }
    public String getRowInfo() { return rowInfo; }
    public String getSeatNumber() { return seatNumber; }
    public String getGateInfo() { return gateInfo; }
    public String getConfirmationCode() { return confirmationCode; }
    public String getTicketType() { return ticketType; }
    public Double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getMembershipNumber() { return membershipNumber; }
    public Integer getCurrentPoints() { return currentPoints; }
    public String getMemberTier() { return memberTier; }
    public Integer getLifetimePoints() { return lifetimePoints; }
    public Double getAccountBalance() { return accountBalance; }
    public String getMemberSince() { return memberSince; }
    public String getFavoriteStore() { return favoriteStore; }
    public String getPromoCode() { return promoCode; }
    public String getCampaignId() { return campaignId; }
    public String getCustomerSegment() { return customerSegment; }
    public Integer getMaxUsageCount() { return maxUsageCount; }
    public String getSourceChannel() { return sourceChannel; }

    public static class Builder {
        private String sectionInfo;
        private String rowInfo;
        private String seatNumber;
        private String gateInfo;
        private String confirmationCode;
        private String ticketType;
        private Double price;
        private String currency;
        private String membershipNumber;
        private Integer currentPoints;
        private String memberTier;
        private Integer lifetimePoints;
        private Double accountBalance;
        private String memberSince;
        private String favoriteStore;
        private String promoCode;
        private String campaignId;
        private String customerSegment;
        private Integer maxUsageCount;
        private String sourceChannel;

        public Builder sectionInfo(String v) { this.sectionInfo = v; return this; }
        public Builder rowInfo(String v) { this.rowInfo = v; return this; }
        public Builder seatNumber(String v) { this.seatNumber = v; return this; }
        public Builder gateInfo(String v) { this.gateInfo = v; return this; }
        public Builder confirmationCode(String v) { this.confirmationCode = v; return this; }
        public Builder ticketType(String v) { this.ticketType = v; return this; }
        public Builder price(Double v) { this.price = v; return this; }
        public Builder currency(String v) { this.currency = v; return this; }
        public Builder membershipNumber(String v) { this.membershipNumber = v; return this; }
        public Builder currentPoints(Integer v) { this.currentPoints = v; return this; }
        public Builder memberTier(String v) { this.memberTier = v; return this; }
        public Builder lifetimePoints(Integer v) { this.lifetimePoints = v; return this; }
        public Builder accountBalance(Double v) { this.accountBalance = v; return this; }
        public Builder memberSince(String v) { this.memberSince = v; return this; }
        public Builder favoriteStore(String v) { this.favoriteStore = v; return this; }
        public Builder promoCode(String v) { this.promoCode = v; return this; }
        public Builder campaignId(String v) { this.campaignId = v; return this; }
        public Builder customerSegment(String v) { this.customerSegment = v; return this; }
        public Builder maxUsageCount(Integer v) { this.maxUsageCount = v; return this; }
        public Builder sourceChannel(String v) { this.sourceChannel = v; return this; }

        public BusinessData build() { return new BusinessData(this); }
    }
}
