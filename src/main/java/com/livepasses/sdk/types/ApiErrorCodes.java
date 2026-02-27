package com.livepasses.sdk.types;

/**
 * All API error codes returned by the Livepasses API.
 * Mirrors ApiErrorCodes.cs 1:1.
 */
public final class ApiErrorCodes {

    private ApiErrorCodes() {
    }

    // General Errors
    public static final String GENERAL_ERROR = "GENERAL_ERROR";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";

    // Authentication & Authorization
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String INVALID_API_KEY = "INVALID_API_KEY";
    public static final String API_KEY_EXPIRED = "API_KEY_EXPIRED";
    public static final String API_KEY_REVOKED = "API_KEY_REVOKED";
    public static final String INSUFFICIENT_PERMISSIONS = "INSUFFICIENT_PERMISSIONS";

    // Validation
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String REQUIRED_FIELD_MISSING = "REQUIRED_FIELD_MISSING";
    public static final String INVALID_FIELD_VALUE = "INVALID_FIELD_VALUE";
    public static final String INVALID_FIELD_FORMAT = "INVALID_FIELD_FORMAT";
    public static final String FIELD_TOO_LONG = "FIELD_TOO_LONG";
    public static final String FIELD_TOO_SHORT = "FIELD_TOO_SHORT";

    // Resource
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String RESOURCE_EXISTS = "RESOURCE_EXISTS";
    public static final String RESOURCE_LOCKED = "RESOURCE_LOCKED";
    public static final String RESOURCE_EXPIRED = "RESOURCE_EXPIRED";

    // Business Rules
    public static final String BUSINESS_RULE_VIOLATION = "BUSINESS_RULE_VIOLATION";
    public static final String INSUFFICIENT_FUNDS = "INSUFFICIENT_FUNDS";
    public static final String QUOTA_EXCEEDED = "QUOTA_EXCEEDED";
    public static final String OPERATION_NOT_ALLOWED = "OPERATION_NOT_ALLOWED";

    // Subscription
    public static final String SUBSCRIPTION_REQUIRED = "SUBSCRIPTION_REQUIRED";
    public static final String FEATURE_NOT_AVAILABLE = "FEATURE_NOT_AVAILABLE";

    // Rate Limiting
    public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    public static final String TOO_MANY_REQUESTS = "TOO_MANY_REQUESTS";
    public static final String API_QUOTA_EXCEEDED = "API_QUOTA_EXCEEDED";

    // Tenant & Partnership
    public static final String TENANT_NOT_FOUND = "TENANT_NOT_FOUND";
    public static final String PARTNERSHIP_NOT_FOUND = "PARTNERSHIP_NOT_FOUND";
    public static final String PARTNERSHIP_INACTIVE = "PARTNERSHIP_INACTIVE";
    public static final String TENANT_SUSPENDED = "TENANT_SUSPENDED";
    public static final String PARTNERSHIP_TIER_LIMIT_REACHED = "PARTNERSHIP_TIER_LIMIT_REACHED";

    // Pass & Template
    public static final String TEMPLATE_NOT_FOUND = "TEMPLATE_NOT_FOUND";
    public static final String PASS_NOT_FOUND = "PASS_NOT_FOUND";
    public static final String PASS_EXPIRED = "PASS_EXPIRED";
    public static final String PASS_ALREADY_USED = "PASS_ALREADY_USED";
    public static final String INVALID_PASS_FORMAT = "INVALID_PASS_FORMAT";
    public static final String TEMPLATE_INACTIVE = "TEMPLATE_INACTIVE";

    // External Services
    public static final String EXTERNAL_SERVICE_ERROR = "EXTERNAL_SERVICE_ERROR";
    public static final String PAYMENT_SERVICE_ERROR = "PAYMENT_SERVICE_ERROR";
    public static final String EMAIL_SERVICE_ERROR = "EMAIL_SERVICE_ERROR";
    public static final String SMS_SERVICE_ERROR = "SMS_SERVICE_ERROR";
    public static final String APPLE_WALLET_ERROR = "APPLE_WALLET_ERROR";
    public static final String GOOGLE_WALLET_ERROR = "GOOGLE_WALLET_ERROR";
}
