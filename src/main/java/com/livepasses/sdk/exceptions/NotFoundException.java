package com.livepasses.sdk.exceptions;

/** Thrown for 404 responses — resource not found. */
public class NotFoundException extends LivepassesException {

    public NotFoundException(String message, String code, String details) {
        super(message, 404, code, details);
    }
}
