package com.livepasses.sdk.exceptions;

/** Thrown for business rule violations (422). */
public class BusinessRuleException extends LivepassesException {

    /** Creates the exception with its historical status, 422. */
    public BusinessRuleException(String message, String code, String details) {
        this(message, code, details, 422);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public BusinessRuleException(String message, String code, String details, int status) {
        super(message, status, code, details);
    }
}
