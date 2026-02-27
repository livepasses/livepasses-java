package com.livepasses.sdk.resources;

import com.livepasses.sdk.internal.LivepassesHttpClient;
import com.livepasses.sdk.types.CreateWebhookParams;
import com.livepasses.sdk.types.Webhook;

import java.util.List;

/**
 * Operations on webhooks: create, list, delete.
 */
public class WebhooksResource {

    private final LivepassesHttpClient http;

    public WebhooksResource(LivepassesHttpClient http) {
        this.http = http;
    }

    /**
     * Create a webhook endpoint.
     */
    public Webhook create(CreateWebhookParams params) {
        return http.post("/api/webhooks", params, Webhook.class);
    }

    /**
     * List all registered webhooks.
     */
    @SuppressWarnings("unchecked")
    public List<Webhook> list() {
        return (List<Webhook>) http.get("/api/webhooks", null, List.class);
    }

    /**
     * Delete a webhook by ID.
     */
    public void delete(String webhookId) {
        http.delete("/api/webhooks/" + webhookId);
    }
}
