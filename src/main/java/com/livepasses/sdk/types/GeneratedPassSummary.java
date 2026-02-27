package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneratedPassSummary {

    private final String id;
    private final String passNumber;
    private final String holderEmail;
    private final String holderName;
    private final String status;
    private final boolean hasApplePass;
    private final boolean hasGooglePass;
    private final String generatedAt;

    @JsonCreator
    public GeneratedPassSummary(
            @JsonProperty("id") String id,
            @JsonProperty("passNumber") String passNumber,
            @JsonProperty("holderEmail") String holderEmail,
            @JsonProperty("holderName") String holderName,
            @JsonProperty("status") String status,
            @JsonProperty("hasApplePass") boolean hasApplePass,
            @JsonProperty("hasGooglePass") boolean hasGooglePass,
            @JsonProperty("generatedAt") String generatedAt) {
        this.id = id;
        this.passNumber = passNumber;
        this.holderEmail = holderEmail;
        this.holderName = holderName;
        this.status = status;
        this.hasApplePass = hasApplePass;
        this.hasGooglePass = hasGooglePass;
        this.generatedAt = generatedAt;
    }

    public String getId() { return id; }
    public String getPassNumber() { return passNumber; }
    public String getHolderEmail() { return holderEmail; }
    public String getHolderName() { return holderName; }
    public String getStatus() { return status; }
    public boolean isHasApplePass() { return hasApplePass; }
    public boolean isHasGooglePass() { return hasGooglePass; }
    public String getGeneratedAt() { return generatedAt; }
}
