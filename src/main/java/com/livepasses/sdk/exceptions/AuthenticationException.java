package com.livepasses.sdk.exceptions;

/** Thrown for 401 responses — invalid or missing API key. */
public class AuthenticationException extends LivepassesException {

    public AuthenticationException(String message, String code, String details) {
        super(message, 401, code, details);
    }
}
