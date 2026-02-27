package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateListItem {

    private final String id;
    private final String name;
    private final String description;
    private final String type;
    private final String status;
    private final int passCount;
    private final String createdAt;
    private final String updatedAt;

    @JsonCreator
    public TemplateListItem(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("type") String type,
            @JsonProperty("status") String status,
            @JsonProperty("passCount") int passCount,
            @JsonProperty("createdAt") String createdAt,
            @JsonProperty("updatedAt") String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.passCount = passCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getPassCount() { return passCount; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
