package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchStatistics {

    private final double averageGenerationTimeSeconds;
    private final double generationSuccessRate;
    private final double deliverySuccessRate;
    private final String mostCommonError;
    private final String totalDuration;

    @JsonCreator
    public BatchStatistics(
            @JsonProperty("averageGenerationTimeSeconds") double averageGenerationTimeSeconds,
            @JsonProperty("generationSuccessRate") double generationSuccessRate,
            @JsonProperty("deliverySuccessRate") double deliverySuccessRate,
            @JsonProperty("mostCommonError") String mostCommonError,
            @JsonProperty("totalDuration") String totalDuration) {
        this.averageGenerationTimeSeconds = averageGenerationTimeSeconds;
        this.generationSuccessRate = generationSuccessRate;
        this.deliverySuccessRate = deliverySuccessRate;
        this.mostCommonError = mostCommonError;
        this.totalDuration = totalDuration;
    }

    public double getAverageGenerationTimeSeconds() { return averageGenerationTimeSeconds; }
    public double getGenerationSuccessRate() { return generationSuccessRate; }
    public double getDeliverySuccessRate() { return deliverySuccessRate; }
    public String getMostCommonError() { return mostCommonError; }
    public String getTotalDuration() { return totalDuration; }
}
