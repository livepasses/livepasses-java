package com.livepasses.sdk.exceptions;

import com.livepasses.sdk.types.ApiErrorCodes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps API error codes and HTTP statuses to typed exceptions.
 */
public final class ExceptionFactory {

    private static final Set<String> AUTH_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.UNAUTHORIZED,
            ApiErrorCodes.INVALID_API_KEY,
            ApiErrorCodes.API_KEY_EXPIRED,
            ApiErrorCodes.API_KEY_REVOKED
    ));

    private static final Set<String> FORBIDDEN_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.FORBIDDEN,
            ApiErrorCodes.INSUFFICIENT_PERMISSIONS
    ));

    private static final Set<String> VALIDATION_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.VALIDATION_ERROR,
            ApiErrorCodes.REQUIRED_FIELD_MISSING,
            ApiErrorCodes.INVALID_FIELD_VALUE,
            ApiErrorCodes.INVALID_FIELD_FORMAT,
            ApiErrorCodes.FIELD_TOO_LONG,
            ApiErrorCodes.FIELD_TOO_SHORT
    ));

    private static final Set<String> NOT_FOUND_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.NOT_FOUND,
            ApiErrorCodes.PASS_NOT_FOUND,
            ApiErrorCodes.TEMPLATE_NOT_FOUND,
            ApiErrorCodes.TENANT_NOT_FOUND,
            ApiErrorCodes.PARTNERSHIP_NOT_FOUND
    ));

    private static final Set<String> RATE_LIMIT_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.RATE_LIMIT_EXCEEDED,
            ApiErrorCodes.TOO_MANY_REQUESTS
    ));

    private static final Set<String> QUOTA_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.QUOTA_EXCEEDED,
            ApiErrorCodes.API_QUOTA_EXCEEDED,
            ApiErrorCodes.SUBSCRIPTION_REQUIRED,
            ApiErrorCodes.FEATURE_NOT_AVAILABLE
    ));

    private static final Set<String> BUSINESS_RULE_CODES = new HashSet<>(Arrays.asList(
            ApiErrorCodes.BUSINESS_RULE_VIOLATION,
            ApiErrorCodes.OPERATION_NOT_ALLOWED,
            ApiErrorCodes.PASS_EXPIRED,
            ApiErrorCodes.PASS_ALREADY_USED,
            ApiErrorCodes.TEMPLATE_INACTIVE,
            ApiErrorCodes.RESOURCE_LOCKED,
            ApiErrorCodes.RESOURCE_EXPIRED
    ));

    private ExceptionFactory() {
    }

    /**
     * Create a typed exception from an API error code and HTTP status.
     */
    public static LivepassesException createTypedException(
            String message, int status, String code, String details, Integer retryAfter) {
        return createTypedException(message, status, code, details, retryAfter, null);
    }

    /**
     * Create a typed exception from an API error code and HTTP status, carrying field-level
     * validation errors (populated only when the API's error.code is VALIDATION_ERROR).
     *
     * <p>The status decides first for 401 and 403 (a 403 carrying UNAUTHORIZED is a permission
     * refusal, not a bad key), then the code, then the remaining statuses. Every exception carries
     * the real status; a 409 conflict has no class of its own and stays a LivepassesException.
     */
    public static LivepassesException createTypedException(
            String message, int status, String code, String details, Integer retryAfter,
            Map<String, List<String>> fields) {

        if (status == 401) return new AuthenticationException(message, code, details, status);
        if (status == 403) return new ForbiddenException(message, code, details, status);

        if (AUTH_CODES.contains(code)) {
            return new AuthenticationException(message, code, details, status);
        }
        if (FORBIDDEN_CODES.contains(code)) {
            return new ForbiddenException(message, code, details, status);
        }
        if (VALIDATION_CODES.contains(code)) {
            return new ValidationException(message, code, details, fields, status);
        }
        if (NOT_FOUND_CODES.contains(code)) {
            return new NotFoundException(message, code, details, status);
        }
        if (RATE_LIMIT_CODES.contains(code)) {
            return new RateLimitException(message, code, retryAfter, details, status);
        }
        if (QUOTA_CODES.contains(code)) {
            return new QuotaExceededException(message, code, details, status);
        }
        if (BUSINESS_RULE_CODES.contains(code)) {
            return new BusinessRuleException(message, code, details, status);
        }

        // Fallback: map by HTTP status
        if (status == 400) return new ValidationException(message, code, details, fields, status);
        if (status == 404) return new NotFoundException(message, code, details, status);
        if (status == 422) return new BusinessRuleException(message, code, details, status);
        if (status == 429) return new RateLimitException(message, code, retryAfter, details, status);

        return new LivepassesException(message, status, code, details);
    }
}
