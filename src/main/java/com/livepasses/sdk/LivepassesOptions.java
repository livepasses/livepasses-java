package com.livepasses.sdk;

import java.time.Duration;

/**
 * Configuration options for the Livepasses client.
 */
public class LivepassesOptions {

    private static final String DEFAULT_BASE_URL = "https://api.livepasses.com";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final int DEFAULT_MAX_RETRIES = 3;

    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;

    private LivepassesOptions(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.timeout = builder.timeout;
        this.maxRetries = builder.maxRetries;
    }

    public static LivepassesOptions defaults() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getBaseUrl() { return baseUrl; }
    public Duration getTimeout() { return timeout; }
    public int getMaxRetries() { return maxRetries; }

    public static class Builder {
        private String baseUrl = DEFAULT_BASE_URL;
        private Duration timeout = DEFAULT_TIMEOUT;
        private int maxRetries = DEFAULT_MAX_RETRIES;

        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }

        public LivepassesOptions build() { return new LivepassesOptions(this); }
    }
}
