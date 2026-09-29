package com.livepasses.sdk.exceptions;

/** Thrown for 401 responses — invalid or missing API key. */
public class AuthenticationException extends LivepassesException {

    /** Creates the exception with its historical status, 401. */
    public AuthenticationException(String message, String code, String details) {
        this(message, code, details, 401);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public AuthenticationException(String message, String code, String details, int status) {
        super(message, status, code, details);
    }
}
