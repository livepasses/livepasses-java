package com.livepasses.sdk.exceptions;

/** Thrown for 403 responses — insufficient permissions. */
public class ForbiddenException extends LivepassesException {

    /** Creates the exception with its historical status, 403. */
    public ForbiddenException(String message, String code, String details) {
        this(message, code, details, 403);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public ForbiddenException(String message, String code, String details, int status) {
        super(message, status, code, details);
    }
}
