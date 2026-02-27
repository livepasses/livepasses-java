package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalPassDto {

    private final String id;
    private final String serialNumber;
    private final String platform;
    private final String status;
    private final String generatedAt;
    private final String redeemedAt;
    private final String validUntil;
    private final String holderEmail;
    private final String templateId;
    private final String templateName;
    private final String templateType;

    @JsonCreator
    public GlobalPassDto(
            @JsonProperty("id") String id,
            @JsonProperty("serialNumber") String serialNumber,
            @JsonProperty("platform") String platform,
            @JsonProperty("status") String status,
            @JsonProperty("generatedAt") String generatedAt,
            @JsonProperty("redeemedAt") String redeemedAt,
            @JsonProperty("validUntil") String validUntil,
            @JsonProperty("holderEmail") String holderEmail,
            @JsonProperty("templateId") String templateId,
            @JsonProperty("templateName") String templateName,
            @JsonProperty("templateType") String templateType) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.platform = platform;
        this.status = status;
        this.generatedAt = generatedAt;
        this.redeemedAt = redeemedAt;
        this.validUntil = validUntil;
        this.holderEmail = holderEmail;
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateType = templateType;
    }

    public String getId() { return id; }
    public String getSerialNumber() { return serialNumber; }
    public String getPlatform() { return platform; }
    public String getStatus() { return status; }
    public String getGeneratedAt() { return generatedAt; }
    public String getRedeemedAt() { return redeemedAt; }
    public String getValidUntil() { return validUntil; }
    public String getHolderEmail() { return holderEmail; }
    public String getTemplateId() { return templateId; }
    public String getTemplateName() { return templateName; }
    public String getTemplateType() { return templateType; }
}
