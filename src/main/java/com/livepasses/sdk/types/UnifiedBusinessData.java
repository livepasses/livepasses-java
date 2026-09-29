package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnifiedBusinessData {

    // Event
    private final String section;
    private final String row;
    private final String seat;
    private final String gate;
    private final String ticketType;
    private final String formattedPrice;
    // Loyalty
    /**
     * Loyalty: identifies the member (max 50 chars, case-insensitive). An existing number links the
     * pass to that member; a new one creates a member; a number belonging to someone whose phone and
     * email both differ is refused with errorCode MEMBERSHIP_NUMBER_CONFLICT. Omit to have one generated.
     */
    private final String membershipNumber;
    private final Integer currentPoints;
    private final String memberTier;
    private final String formattedBalance;
    // Coupon
    private final String promoCode;
    private final String discountDescription;
    private final String validityDescription;

    @JsonCreator
    public UnifiedBusinessData(
            @JsonProperty("section") String section,
            @JsonProperty("row") String row,
            @JsonProperty("seat") String seat,
            @JsonProperty("gate") String gate,
            @JsonProperty("ticketType") String ticketType,
            @JsonProperty("formattedPrice") String formattedPrice,
            @JsonProperty("membershipNumber") String membershipNumber,
            @JsonProperty("currentPoints") Integer currentPoints,
            @JsonProperty("memberTier") String memberTier,
            @JsonProperty("formattedBalance") String formattedBalance,
            @JsonProperty("promoCode") String promoCode,
            @JsonProperty("discountDescription") String discountDescription,
            @JsonProperty("validityDescription") String validityDescription) {
        this.section = section;
        this.row = row;
        this.seat = seat;
        this.gate = gate;
        this.ticketType = ticketType;
        this.formattedPrice = formattedPrice;
        this.membershipNumber = membershipNumber;
        this.currentPoints = currentPoints;
        this.memberTier = memberTier;
        this.formattedBalance = formattedBalance;
        this.promoCode = promoCode;
        this.discountDescription = discountDescription;
        this.validityDescription = validityDescription;
    }

    public String getSection() { return section; }
    public String getRow() { return row; }
    public String getSeat() { return seat; }
    public String getGate() { return gate; }
    public String getTicketType() { return ticketType; }
    public String getFormattedPrice() { return formattedPrice; }
    public String getMembershipNumber() { return membershipNumber; }
    public Integer getCurrentPoints() { return currentPoints; }
    public String getMemberTier() { return memberTier; }
    public String getFormattedBalance() { return formattedBalance; }
    public String getPromoCode() { return promoCode; }
    public String getDiscountDescription() { return discountDescription; }
    public String getValidityDescription() { return validityDescription; }
}
