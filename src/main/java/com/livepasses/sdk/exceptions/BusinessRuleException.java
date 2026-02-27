package com.livepasses.sdk.exceptions;

/** Thrown for business rule violations (422). */
public class BusinessRuleException extends LivepassesException {

    public BusinessRuleException(String message, String code, String details) {
        super(message, 422, code, details);
    }
}
