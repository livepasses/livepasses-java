package com.livepasses.sdk.types;

import java.util.function.Consumer;

/**
 * Options for generateAndWait polling behavior.
 */
public class GenerateAndWaitOptions {

    private final long pollIntervalMs;
    private final int maxAttempts;
    private final Consumer<BatchStatusResult> onProgress;

    private GenerateAndWaitOptions(Builder builder) {
        this.pollIntervalMs = builder.pollIntervalMs;
        this.maxAttempts = builder.maxAttempts;
        this.onProgress = builder.onProgress;
    }

    public static Builder builder() { return new Builder(); }

    /** Polling interval in milliseconds. Default: 2000. */
    public long getPollIntervalMs() { return pollIntervalMs; }

    /** Maximum number of poll attempts. Default: 150 (5 minutes at 2s interval). */
    public int getMaxAttempts() { return maxAttempts; }

    /** Callback invoked on each poll with current batch status. May be null. */
    public Consumer<BatchStatusResult> getOnProgress() { return onProgress; }

    public static class Builder {
        private long pollIntervalMs = 2000;
        private int maxAttempts = 150;
        private Consumer<BatchStatusResult> onProgress;

        public Builder pollIntervalMs(long v) { this.pollIntervalMs = v; return this; }
        public Builder maxAttempts(int v) { this.maxAttempts = v; return this; }
        public Builder onProgress(Consumer<BatchStatusResult> v) { this.onProgress = v; return this; }

        public GenerateAndWaitOptions build() { return new GenerateAndWaitOptions(this); }
    }
}
