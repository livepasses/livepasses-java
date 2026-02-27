package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassValidationResult {

    private final String passId;
    private final String passNumber;
    private final String status;
    private final boolean canBeRedeemed;
    private final boolean isExpired;
    private final String validationMessage;
    private final String templateType;
    private final String holderName;
    private final String holderEmail;
    private final String validFrom;
    private final String validUntil;
    private final List<String> verificationMethods;

    @JsonCreator
    public PassValidationResult(
            @JsonProperty("passId") String passId,
            @JsonProperty("passNumber") String passNumber,
            @JsonProperty("status") String status,
            @JsonProperty("canBeRedeemed") boolean canBeRedeemed,
            @JsonProperty("isExpired") boolean isExpired,
            @JsonProperty("validationMessage") String validationMessage,
            @JsonProperty("templateType") String templateType,
            @JsonProperty("holderName") String holderName,
            @JsonProperty("holderEmail") String holderEmail,
            @JsonProperty("validFrom") String validFrom,
            @JsonProperty("validUntil") String validUntil,
            @JsonProperty("verificationMethods") List<String> verificationMethods) {
        this.passId = passId;
        this.passNumber = passNumber;
        this.status = status;
        this.canBeRedeemed = canBeRedeemed;
        this.isExpired = isExpired;
        this.validationMessage = validationMessage;
        this.templateType = templateType;
        this.holderName = holderName;
        this.holderEmail = holderEmail;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.verificationMethods = verificationMethods != null ? verificationMethods : Collections.emptyList();
    }

    public String getPassId() { return passId; }
    public String getPassNumber() { return passNumber; }
    public String getStatus() { return status; }
    public boolean isCanBeRedeemed() { return canBeRedeemed; }
    public boolean isExpired() { return isExpired; }
    public String getValidationMessage() { return validationMessage; }
    public String getTemplateType() { return templateType; }
    public String getHolderName() { return holderName; }
    public String getHolderEmail() { return holderEmail; }
    public String getValidFrom() { return validFrom; }
    public String getValidUntil() { return validUntil; }
    public List<String> getVerificationMethods() { return verificationMethods; }
}
