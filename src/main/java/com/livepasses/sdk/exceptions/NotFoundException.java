package com.livepasses.sdk.exceptions;

/** Thrown for 404 responses — resource not found. */
public class NotFoundException extends LivepassesException {

    /** Creates the exception with its historical status, 404. */
    public NotFoundException(String message, String code, String details) {
        this(message, code, details, 404);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public NotFoundException(String message, String code, String details, int status) {
        super(message, status, code, details);
    }
}
