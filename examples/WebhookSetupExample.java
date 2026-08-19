package com.livepasses.sdk.examples;

import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;

import java.util.List;

/**
 * Register, list, and manage webhooks.
 *
 * Run: export LIVEPASSES_API_KEY="your-api-key"
 *      ./gradlew run -PmainClass=com.livepasses.sdk.examples.WebhookSetupExample
 */
public class WebhookSetupExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("LIVEPASSES_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Set LIVEPASSES_API_KEY environment variable");
            return;
        }

        Livepasses client = new Livepasses(apiKey);

        try {
            // 1. Create a webhook for pass events
            System.out.println("Registering webhook...");
            Webhook webhook = client.webhooks().create(
                    CreateWebhookParams.builder()
                            .url("https://your-app.com/webhooks/livepasses")
                            .events(List.of(
                                    WebhookEventType.PASS_GENERATED,
                                    WebhookEventType.PASS_REDEEMED,
                                    WebhookEventType.PASS_UPDATED,
                                    WebhookEventType.TRANSFER_ACCEPTED))
                            .build());
            System.out.printf("  Webhook ID: %s%n", webhook.getId());
            System.out.printf("  URL: %s%n", webhook.getUrl());
            System.out.printf("  Events: %s%n", webhook.getEvents());
            System.out.printf("  Secret: %s%n", webhook.getSecret());
            System.out.println("  (Store this secret to verify incoming webhook signatures)\n");

            // 2. List all registered webhooks
            System.out.println("Listing all webhooks...");
            List<Webhook> webhooks = client.webhooks().list();
            for (Webhook wh : webhooks) {
                System.out.printf("  - %s: %s (active: %s)%n", wh.getId(), wh.getUrl(), wh.isActive());
                System.out.printf("    Events: %s%n", wh.getEvents());
            }
            System.out.println();

            // 3. Clean up — delete the webhook
            System.out.println("Deleting webhook...");
            client.webhooks().delete(webhook.getId());
            System.out.println("  Webhook deleted\n");

            System.out.println("Done!");

        } catch (LivepassesException e) {
            System.err.printf("API error [%s]: %s%n", e.getCode(), e.getMessage());
        }
    }
}
