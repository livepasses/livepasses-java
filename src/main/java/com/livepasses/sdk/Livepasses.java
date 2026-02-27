package com.livepasses.sdk;

import com.livepasses.sdk.internal.LivepassesHttpClient;
import com.livepasses.sdk.resources.PassesResource;
import com.livepasses.sdk.resources.TemplatesResource;
import com.livepasses.sdk.resources.WebhooksResource;

/**
 * Livepasses API client.
 *
 * <pre>{@code
 * Livepasses client = new Livepasses("lp_api_key_...");
 *
 * PassGenerationResult result = client.passes().generate(
 *     GeneratePassesParams.builder()
 *         .templateId("template-id")
 *         .passes(List.of(
 *             PassRecipient.builder()
 *                 .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
 *                 .businessData(BusinessData.builder().sectionInfo("A").rowInfo("12").build())
 *                 .build()
 *         ))
 *         .build()
 * );
 * }</pre>
 */
public class Livepasses {

    private final PassesResource passes;
    private final TemplatesResource templates;
    private final WebhooksResource webhooks;

    /**
     * Create a Livepasses client with default options.
     *
     * @param apiKey your API key (get one at https://dashboard.livepasses.com/api-keys)
     */
    public Livepasses(String apiKey) {
        this(apiKey, LivepassesOptions.defaults());
    }

    /**
     * Create a Livepasses client with custom options.
     *
     * @param apiKey  your API key
     * @param options custom configuration (base URL, timeout, retries)
     */
    public Livepasses(String apiKey, LivepassesOptions options) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "An API key is required. Get yours at https://dashboard.livepasses.com/api-keys");
        }

        LivepassesHttpClient http = new LivepassesHttpClient(
                apiKey,
                options.getBaseUrl(),
                options.getTimeout(),
                options.getMaxRetries()
        );

        this.passes = new PassesResource(http);
        this.templates = new TemplatesResource(http);
        this.webhooks = new WebhooksResource(http);
    }

    /** Pass generation, listing, lookup, validation, and redemption. */
    public PassesResource passes() { return passes; }

    /** Template management: list, create, update, activate/deactivate. */
    public TemplatesResource templates() { return templates; }

    /** Webhook registration and management. */
    public WebhooksResource webhooks() { return webhooks; }
}
