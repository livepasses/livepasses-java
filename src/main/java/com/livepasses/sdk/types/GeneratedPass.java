package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratedPass {

    private final String id;
    private final String customerEmail;
    private final String confirmationCode;
    private final PassPlatforms platforms;
    private final UnifiedBusinessData businessData;
    private final String qrCode;
    private final String status;
    private final AnalyticsInfo analytics;

    @JsonCreator
    public GeneratedPass(
            @JsonProperty("id") String id,
            @JsonProperty("customerEmail") String customerEmail,
            @JsonProperty("confirmationCode") String confirmationCode,
            @JsonProperty("platforms") PassPlatforms platforms,
            @JsonProperty("businessData") UnifiedBusinessData businessData,
            @JsonProperty("qrCode") String qrCode,
            @JsonProperty("status") String status,
            @JsonProperty("analytics") AnalyticsInfo analytics) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.confirmationCode = confirmationCode;
        this.platforms = platforms;
        this.businessData = businessData;
        this.qrCode = qrCode;
        this.status = status;
        this.analytics = analytics;
    }

    public String getId() { return id; }
    public String getCustomerEmail() { return customerEmail; }
    public String getConfirmationCode() { return confirmationCode; }
    public PassPlatforms getPlatforms() { return platforms; }
    public UnifiedBusinessData getBusinessData() { return businessData; }
    public String getQrCode() { return qrCode; }
    public String getStatus() { return status; }
    public AnalyticsInfo getAnalytics() { return analytics; }
}
