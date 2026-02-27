package com.livepasses.sdk.exceptions;

/** Thrown for 403 responses — insufficient permissions. */
public class ForbiddenException extends LivepassesException {

    public ForbiddenException(String message, String code, String details) {
        super(message, 403, code, details);
    }
}
