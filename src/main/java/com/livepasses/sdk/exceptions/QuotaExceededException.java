package com.livepasses.sdk.exceptions;

/** Thrown when subscription quota is exceeded. The API answers 422; {@link #getStatus()} carries the real status. */
public class QuotaExceededException extends LivepassesException {

    /** Creates the exception with its historical status, 403. */
    public QuotaExceededException(String message, String code, String details) {
        this(message, code, details, 403);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public QuotaExceededException(String message, String code, String details, int status) {
        super(message, status, code, details);
    }
}
