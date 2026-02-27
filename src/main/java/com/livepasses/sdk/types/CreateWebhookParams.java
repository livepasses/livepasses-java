package com.livepasses.sdk.types;

import java.util.List;

public class CreateWebhookParams {

    private final String url;
    private final List<WebhookEventType> events;

    private CreateWebhookParams(Builder builder) {
        this.url = builder.url;
        this.events = builder.events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUrl() { return url; }
    public List<WebhookEventType> getEvents() { return events; }

    public static class Builder {
        private String url;
        private List<WebhookEventType> events;

        public Builder url(String url) { this.url = url; return this; }
        public Builder events(List<WebhookEventType> events) { this.events = events; return this; }

        public CreateWebhookParams build() {
            if (url == null) throw new IllegalArgumentException("url is required");
            if (events == null || events.isEmpty()) throw new IllegalArgumentException("events is required");
            return new CreateWebhookParams(this);
        }
    }
}
