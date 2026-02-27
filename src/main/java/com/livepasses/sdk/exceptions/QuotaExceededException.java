package com.livepasses.sdk.exceptions;

/** Thrown when subscription quota is exceeded. */
public class QuotaExceededException extends LivepassesException {

    public QuotaExceededException(String message, String code, String details) {
        super(message, 403, code, details);
    }
}
