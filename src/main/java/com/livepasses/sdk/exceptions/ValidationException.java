package com.livepasses.sdk.exceptions;

/** Thrown for validation errors (400) — invalid input data. */
public class ValidationException extends LivepassesException {

    public ValidationException(String message, String code, String details) {
        super(message, 400, code, details);
    }
}
