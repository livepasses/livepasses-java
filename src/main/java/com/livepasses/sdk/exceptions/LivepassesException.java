package com.livepasses.sdk.exceptions;

/**
 * Base exception for all Livepasses API errors.
 * Contains the HTTP status code, API error code, and optional details.
 */
public class LivepassesException extends RuntimeException {

    private final int status;
    private final String code;
    private final String details;

    public LivepassesException(String message, int status, String code) {
        this(message, status, code, null);
    }

    public LivepassesException(String message, int status, String code, String details) {
        super(message);
        this.status = status;
        this.code = code;
        this.details = details;
    }

    /** HTTP status code (e.g. 401, 404, 429). */
    public int getStatus() {
        return status;
    }

    /** API error code (e.g. "UNAUTHORIZED", "VALIDATION_ERROR"). */
    public String getCode() {
        return code;
    }

    /** Additional error details, if provided by the API. */
    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[").append(status).append(" ").append(code).append("]: ").append(getMessage());
        if (details != null) {
            sb.append(" (").append(details).append(")");
        }
        return sb.toString();
    }
}
