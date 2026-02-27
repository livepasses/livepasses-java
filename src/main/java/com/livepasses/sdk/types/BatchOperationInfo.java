package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchOperationInfo {

    private final String id;
    private final String status;
    private final int totalRecipients;
    private final int passesGenerated;
    private final int generationFailures;
    private final double progressPercentage;
    private final String estimatedCompletion;
    private final String statusPollUrl;

    @JsonCreator
    public BatchOperationInfo(
            @JsonProperty("id") String id,
            @JsonProperty("status") String status,
            @JsonProperty("totalRecipients") int totalRecipients,
            @JsonProperty("passesGenerated") int passesGenerated,
            @JsonProperty("generationFailures") int generationFailures,
            @JsonProperty("progressPercentage") double progressPercentage,
            @JsonProperty("estimatedCompletion") String estimatedCompletion,
            @JsonProperty("statusPollUrl") String statusPollUrl) {
        this.id = id;
        this.status = status;
        this.totalRecipients = totalRecipients;
        this.passesGenerated = passesGenerated;
        this.generationFailures = generationFailures;
        this.progressPercentage = progressPercentage;
        this.estimatedCompletion = estimatedCompletion;
        this.statusPollUrl = statusPollUrl;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public int getTotalRecipients() { return totalRecipients; }
    public int getPassesGenerated() { return passesGenerated; }
    public int getGenerationFailures() { return generationFailures; }
    public double getProgressPercentage() { return progressPercentage; }
    public String getEstimatedCompletion() { return estimatedCompletion; }
    public String getStatusPollUrl() { return statusPollUrl; }
}
