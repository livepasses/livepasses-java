package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Webhook {

    private final String id;
    private final String url;
    private final List<WebhookEventType> events;
    private final boolean isActive;
    private final String createdAt;
    private final String secret;

    @JsonCreator
    public Webhook(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("events") List<WebhookEventType> events,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("createdAt") String createdAt,
            @JsonProperty("secret") String secret) {
        this.id = id;
        this.url = url;
        this.events = events;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.secret = secret;
    }

    public String getId() { return id; }
    public String getUrl() { return url; }
    public List<WebhookEventType> getEvents() { return events; }
    public boolean isActive() { return isActive; }
    public String getCreatedAt() { return createdAt; }
    public String getSecret() { return secret; }
}
