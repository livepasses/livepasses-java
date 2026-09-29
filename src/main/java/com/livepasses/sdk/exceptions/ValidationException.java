package com.livepasses.sdk.exceptions;

import java.util.List;
import java.util.Map;

/** Thrown for validation errors (400) — invalid input data. */
public class ValidationException extends LivepassesException {

    /**
     * Field path -> validation messages, keyed by the API's camelCase field path (for example
     * {@code operations[0].path}). Present only when the API's error.code is VALIDATION_ERROR.
     */
    private final Map<String, List<String>> fields;

    public ValidationException(String message, String code, String details) {
        this(message, code, details, null);
    }

    /** Creates the exception with its historical status, 400. */
    public ValidationException(String message, String code, String details, Map<String, List<String>> fields) {
        this(message, code, details, fields, 400);
    }

    /** Creates the exception carrying the response's real HTTP status. */
    public ValidationException(String message, String code, String details, Map<String, List<String>> fields, int status) {
        super(message, status, code, details);
        this.fields = fields;
    }

    public Map<String, List<String>> getFields() {
        return fields;
    }
}
