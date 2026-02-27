package com.livepasses.sdk.exceptions;

/** Thrown for 429 responses — rate limit exceeded. */
public class RateLimitException extends LivepassesException {

    private final Integer retryAfter;

    public RateLimitException(String message, String code, Integer retryAfter, String details) {
        super(message, 429, code, details);
        this.retryAfter = retryAfter;
    }

    /** Seconds to wait before retrying, from the Retry-After header. May be null. */
    public Integer getRetryAfter() {
        return retryAfter;
    }
}
