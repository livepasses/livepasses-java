package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateDetail {

    private final String id;
    private final String name;
    private final String description;
    private final String type;
    private final String status;
    private final int passCount;
    private final String createdAt;
    private final String updatedAt;
    private final Map<String, Object> businessFeatures;
    private final Map<String, Object> platformSupport;
    private final Map<String, Object> mediaConfiguration;

    @JsonCreator
    public TemplateDetail(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("type") String type,
            @JsonProperty("status") String status,
            @JsonProperty("passCount") int passCount,
            @JsonProperty("createdAt") String createdAt,
            @JsonProperty("updatedAt") String updatedAt,
            @JsonProperty("businessFeatures") Map<String, Object> businessFeatures,
            @JsonProperty("platformSupport") Map<String, Object> platformSupport,
            @JsonProperty("mediaConfiguration") Map<String, Object> mediaConfiguration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.passCount = passCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.businessFeatures = businessFeatures;
        this.platformSupport = platformSupport;
        this.mediaConfiguration = mediaConfiguration;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getPassCount() { return passCount; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public Map<String, Object> getBusinessFeatures() { return businessFeatures; }
    public Map<String, Object> getPlatformSupport() { return platformSupport; }
    public Map<String, Object> getMediaConfiguration() { return mediaConfiguration; }
}
