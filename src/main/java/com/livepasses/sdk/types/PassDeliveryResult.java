package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassDeliveryResult {

    private final String method;
    private final String status;
    private final String sentAt;
    private final List<DeliveryDetail> details;

    @JsonCreator
    public PassDeliveryResult(
            @JsonProperty("method") String method,
            @JsonProperty("status") String status,
            @JsonProperty("sentAt") String sentAt,
            @JsonProperty("details") List<DeliveryDetail> details) {
        this.method = method;
        this.status = status;
        this.sentAt = sentAt;
        this.details = details;
    }

    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public String getSentAt() { return sentAt; }
    public List<DeliveryDetail> getDetails() { return details; }
}
