package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsInfo {

    private final String trackingId;
    private final String engagementUrl;

    @JsonCreator
    public AnalyticsInfo(
            @JsonProperty("trackingId") String trackingId,
            @JsonProperty("engagementUrl") String engagementUrl) {
        this.trackingId = trackingId;
        this.engagementUrl = engagementUrl;
    }

    public String getTrackingId() { return trackingId; }
    public String getEngagementUrl() { return engagementUrl; }
}
