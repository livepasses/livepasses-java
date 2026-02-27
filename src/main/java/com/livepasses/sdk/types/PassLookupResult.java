package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassLookupResult {

    private final String passId;
    private final String passNumber;
    private final String templateId;
    private final String templateName;
    private final String templateType;
    private final String holderName;
    private final String holderEmail;
    private final String status;
    private final boolean isValid;
    private final boolean canBeRedeemed;
    private final boolean isExpired;
    private final String validFrom;
    private final String validUntil;
    private final String redeemedAt;
    private final String generatedAt;

    @JsonCreator
    public PassLookupResult(
            @JsonProperty("passId") String passId,
            @JsonProperty("passNumber") String passNumber,
            @JsonProperty("templateId") String templateId,
            @JsonProperty("templateName") String templateName,
            @JsonProperty("templateType") String templateType,
            @JsonProperty("holderName") String holderName,
            @JsonProperty("holderEmail") String holderEmail,
            @JsonProperty("status") String status,
            @JsonProperty("isValid") boolean isValid,
            @JsonProperty("canBeRedeemed") boolean canBeRedeemed,
            @JsonProperty("isExpired") boolean isExpired,
            @JsonProperty("validFrom") String validFrom,
            @JsonProperty("validUntil") String validUntil,
            @JsonProperty("redeemedAt") String redeemedAt,
            @JsonProperty("generatedAt") String generatedAt) {
        this.passId = passId;
        this.passNumber = passNumber;
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateType = templateType;
        this.holderName = holderName;
        this.holderEmail = holderEmail;
        this.status = status;
        this.isValid = isValid;
        this.canBeRedeemed = canBeRedeemed;
        this.isExpired = isExpired;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.redeemedAt = redeemedAt;
        this.generatedAt = generatedAt;
    }

    public String getPassId() { return passId; }
    public String getPassNumber() { return passNumber; }
    public String getTemplateId() { return templateId; }
    public String getTemplateName() { return templateName; }
    public String getTemplateType() { return templateType; }
    public String getHolderName() { return holderName; }
    public String getHolderEmail() { return holderEmail; }
    public String getStatus() { return status; }
    public boolean isValid() { return isValid; }
    public boolean isCanBeRedeemed() { return canBeRedeemed; }
    public boolean isExpired() { return isExpired; }
    public String getValidFrom() { return validFrom; }
    public String getValidUntil() { return validUntil; }
    public String getRedeemedAt() { return redeemedAt; }
    public String getGeneratedAt() { return generatedAt; }
}
