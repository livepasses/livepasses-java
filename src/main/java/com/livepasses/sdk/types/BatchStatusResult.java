package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchStatusResult {

    private final String id;
    private final String status;
    private final int totalRecipients;
    private final int passesGenerated;
    private final int passesDelivered;
    private final int generationFailures;
    private final int deliveryFailures;
    private final double progressPercentage;
    private final String startedAt;
    private final String completedAt;
    private final String estimatedCompletion;
    private final String errorMessage;
    private final boolean isCompleted;
    private final boolean isActive;
    private final BatchStatistics statistics;
    private final List<GeneratedPassSummary> generatedPasses;

    @JsonCreator
    public BatchStatusResult(
            @JsonProperty("id") String id,
            @JsonProperty("status") String status,
            @JsonProperty("totalRecipients") int totalRecipients,
            @JsonProperty("passesGenerated") int passesGenerated,
            @JsonProperty("passesDelivered") int passesDelivered,
            @JsonProperty("generationFailures") int generationFailures,
            @JsonProperty("deliveryFailures") int deliveryFailures,
            @JsonProperty("progressPercentage") double progressPercentage,
            @JsonProperty("startedAt") String startedAt,
            @JsonProperty("completedAt") String completedAt,
            @JsonProperty("estimatedCompletion") String estimatedCompletion,
            @JsonProperty("errorMessage") String errorMessage,
            @JsonProperty("isCompleted") boolean isCompleted,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("statistics") BatchStatistics statistics,
            @JsonProperty("generatedPasses") List<GeneratedPassSummary> generatedPasses) {
        this.id = id;
        this.status = status;
        this.totalRecipients = totalRecipients;
        this.passesGenerated = passesGenerated;
        this.passesDelivered = passesDelivered;
        this.generationFailures = generationFailures;
        this.deliveryFailures = deliveryFailures;
        this.progressPercentage = progressPercentage;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.estimatedCompletion = estimatedCompletion;
        this.errorMessage = errorMessage;
        this.isCompleted = isCompleted;
        this.isActive = isActive;
        this.statistics = statistics;
        this.generatedPasses = generatedPasses != null ? generatedPasses : Collections.emptyList();
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public int getTotalRecipients() { return totalRecipients; }
    public int getPassesGenerated() { return passesGenerated; }
    public int getPassesDelivered() { return passesDelivered; }
    public int getGenerationFailures() { return generationFailures; }
    public int getDeliveryFailures() { return deliveryFailures; }
    public double getProgressPercentage() { return progressPercentage; }
    public String getStartedAt() { return startedAt; }
    public String getCompletedAt() { return completedAt; }
    public String getEstimatedCompletion() { return estimatedCompletion; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isCompleted() { return isCompleted; }
    public boolean isActive() { return isActive; }
    public BatchStatistics getStatistics() { return statistics; }
    public List<GeneratedPassSummary> getGeneratedPasses() { return generatedPasses; }
}
