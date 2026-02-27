package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassRedemptionResult {

    private final String passId;
    private final String passNumber;
    private final String redeemedAt;
    private final String redemptionMethod;
    private final String previousStatus;
    private final String newStatus;
    private final boolean alreadyRedeemed;

    @JsonCreator
    public PassRedemptionResult(
            @JsonProperty("passId") String passId,
            @JsonProperty("passNumber") String passNumber,
            @JsonProperty("redeemedAt") String redeemedAt,
            @JsonProperty("redemptionMethod") String redemptionMethod,
            @JsonProperty("previousStatus") String previousStatus,
            @JsonProperty("newStatus") String newStatus,
            @JsonProperty("alreadyRedeemed") boolean alreadyRedeemed) {
        this.passId = passId;
        this.passNumber = passNumber;
        this.redeemedAt = redeemedAt;
        this.redemptionMethod = redemptionMethod;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.alreadyRedeemed = alreadyRedeemed;
    }

    public String getPassId() { return passId; }
    public String getPassNumber() { return passNumber; }
    public String getRedeemedAt() { return redeemedAt; }
    public String getRedemptionMethod() { return redemptionMethod; }
    public String getPreviousStatus() { return previousStatus; }
    public String getNewStatus() { return newStatus; }
    public boolean isAlreadyRedeemed() { return alreadyRedeemed; }
}
