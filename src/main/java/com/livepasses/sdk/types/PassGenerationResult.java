package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassGenerationResult {

    private final String batchId;
    private final String templateId;
    private final String generatedAt;
    private final int totalPasses;
    private final boolean isAsyncProcessing;
    private final BatchOperationInfo batchOperation;
    private List<GeneratedPass> passes;
    private final PassDeliveryResult delivery;
    private final BusinessMetrics businessMetrics;

    @JsonCreator
    public PassGenerationResult(
            @JsonProperty("batchId") String batchId,
            @JsonProperty("templateId") String templateId,
            @JsonProperty("generatedAt") String generatedAt,
            @JsonProperty("totalPasses") int totalPasses,
            @JsonProperty("isAsyncProcessing") boolean isAsyncProcessing,
            @JsonProperty("batchOperation") BatchOperationInfo batchOperation,
            @JsonProperty("passes") List<GeneratedPass> passes,
            @JsonProperty("delivery") PassDeliveryResult delivery,
            @JsonProperty("businessMetrics") BusinessMetrics businessMetrics) {
        this.batchId = batchId;
        this.templateId = templateId;
        this.generatedAt = generatedAt;
        this.totalPasses = totalPasses;
        this.isAsyncProcessing = isAsyncProcessing;
        this.batchOperation = batchOperation;
        this.passes = passes != null ? passes : Collections.emptyList();
        this.delivery = delivery;
        this.businessMetrics = businessMetrics;
    }

    public String getBatchId() { return batchId; }
    public String getTemplateId() { return templateId; }
    public String getGeneratedAt() { return generatedAt; }
    public int getTotalPasses() { return totalPasses; }
    public boolean isAsyncProcessing() { return isAsyncProcessing; }
    public BatchOperationInfo getBatchOperation() { return batchOperation; }
    public List<GeneratedPass> getPasses() { return passes; }
    public PassDeliveryResult getDelivery() { return delivery; }
    public BusinessMetrics getBusinessMetrics() { return businessMetrics; }

    /** Used internally by generateAndWait to merge batch results. */
    public void setPasses(List<GeneratedPass> passes) {
        this.passes = passes;
    }
}
