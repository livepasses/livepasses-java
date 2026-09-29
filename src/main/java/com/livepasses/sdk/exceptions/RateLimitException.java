package com.livepasses.sdk.exceptions;

/** Thrown for 429 responses — rate limit exceeded. */
public class RateLimitException extends LivepassesException {

    private final Integer retryAfter;

    /** Creates the exception with its historical status, 429. */
    public RateLimitException(String message, String code, Integer retryAfter, String details) {
        this(message, code, retryAfter, details, 429);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public RateLimitException(String message, String code, Integer retryAfter, String details, int status) {
        super(message, status, code, details);
        this.retryAfter = retryAfter;
    }

    /** Seconds to wait before retrying, from the Retry-After header. May be null. */
    public Integer getRetryAfter() {
        return retryAfter;
    }
}
