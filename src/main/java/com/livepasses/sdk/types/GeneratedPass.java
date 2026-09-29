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
    /** Present when status is "failed", e.g. "MEMBERSHIP_NUMBER_CONFLICT" */
    private final String errorCode;
    /** Present when status is "failed" */
    private final String errorMessage;
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
            @JsonProperty("errorCode") String errorCode,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("analytics") AnalyticsInfo analytics) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.confirmationCode = confirmationCode;
        this.platforms = platforms;
        this.businessData = businessData;
        this.qrCode = qrCode;
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.analytics = analytics;
    }

    /**
     * The constructor from before {@code errorCode} and {@code errorMessage} existed, kept so code
     * compiled against it keeps linking. Both error fields are null.
     */
    public GeneratedPass(
            String id,
            String customerEmail,
            String confirmationCode,
            PassPlatforms platforms,
            UnifiedBusinessData businessData,
            String qrCode,
            String status,
            AnalyticsInfo analytics) {
        this(id, customerEmail, confirmationCode, platforms, businessData, qrCode, status, null, null, analytics);
    }

    public String getId() { return id; }
    public String getCustomerEmail() { return customerEmail; }
    public String getConfirmationCode() { return confirmationCode; }
    public PassPlatforms getPlatforms() { return platforms; }
    public UnifiedBusinessData getBusinessData() { return businessData; }
    public String getQrCode() { return qrCode; }
    public String getStatus() { return status; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public AnalyticsInfo getAnalytics() { return analytics; }
}
