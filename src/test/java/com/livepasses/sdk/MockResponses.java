package com.livepasses.sdk;

/**
 * Factory for mock JSON responses used in tests.
 */
public final class MockResponses {

    private MockResponses() {
    }

    public static String successEnvelope(String data) {
        return "{\"success\":true,\"data\":" + data + ",\"message\":\"Success\"}";
    }

    public static String pagedEnvelope(String items, int currentPage, int pageSize, int totalPages, int totalItems) {
        return "{\"success\":true,\"items\":" + items
                + ",\"pagination\":{\"currentPage\":" + currentPage
                + ",\"pageSize\":" + pageSize
                + ",\"totalPages\":" + totalPages
                + ",\"totalItems\":" + totalItems + "}}";
    }

    public static String errorEnvelope(String code, String message, String details) {
        return "{\"success\":false,\"error\":{\"code\":\"" + code
                + "\",\"message\":\"" + message + "\""
                + (details != null ? ",\"details\":\"" + details + "\"" : "")
                + "}}";
    }

    public static String passGenerationResult(boolean isAsync) {
        if (isAsync) {
            return "{\"batchId\":\"batch-123\",\"templateId\":\"tpl-1\",\"generatedAt\":\"2026-01-01T00:00:00Z\""
                    + ",\"totalPasses\":5,\"isAsyncProcessing\":true,\"passes\":[]}";
        }
        return "{\"batchId\":\"batch-123\",\"templateId\":\"tpl-1\",\"generatedAt\":\"2026-01-01T00:00:00Z\""
                + ",\"totalPasses\":1,\"isAsyncProcessing\":false"
                + ",\"passes\":[{\"id\":\"pass-1\",\"status\":\"active\""
                + ",\"platforms\":{\"apple\":{\"available\":true,\"addToWalletUrl\":\"https://apple.example.com\",\"features\":[\"qr\"]}"
                + ",\"google\":{\"available\":true,\"addToWalletUrl\":\"https://google.example.com\",\"features\":[\"qr\"]}}"
                + ",\"businessData\":{\"section\":\"VIP\",\"row\":\"A\",\"seat\":\"1\"}}]}";
    }

    public static String batchStatusResult(boolean isCompleted, double progressPercentage) {
        return "{\"id\":\"batch-123\",\"status\":\"" + (isCompleted ? "completed" : "processing") + "\""
                + ",\"totalRecipients\":5,\"passesGenerated\":" + (isCompleted ? 5 : 2)
                + ",\"passesDelivered\":0,\"generationFailures\":0,\"deliveryFailures\":0"
                + ",\"progressPercentage\":" + progressPercentage
                + ",\"isCompleted\":" + isCompleted + ",\"isActive\":" + !isCompleted
                + ",\"generatedPasses\":" + (isCompleted
                ? "[{\"id\":\"p-1\",\"passNumber\":\"LP-001\",\"status\":\"active\",\"hasApplePass\":true,\"hasGooglePass\":true,\"generatedAt\":\"2026-01-01T00:00:00Z\"}]"
                : "[]")
                + "}";
    }

    public static String passLookupResult() {
        return "{\"passId\":\"pass-1\",\"passNumber\":\"LP-001\",\"templateId\":\"tpl-1\""
                + ",\"templateName\":\"Event Pass\",\"templateType\":\"event\""
                + ",\"holderName\":\"Jane Doe\",\"holderEmail\":\"jane@example.com\""
                + ",\"status\":\"active\",\"isValid\":true,\"canBeRedeemed\":true,\"isExpired\":false"
                + ",\"generatedAt\":\"2026-01-01T00:00:00Z\"}";
    }

    public static String passValidationResult() {
        return "{\"passId\":\"pass-1\",\"passNumber\":\"LP-001\",\"status\":\"active\""
                + ",\"canBeRedeemed\":true,\"isExpired\":false"
                + ",\"validationMessage\":\"Pass is valid for redemption\""
                + ",\"templateType\":\"event\""
                + ",\"verificationMethods\":[\"qr_code\",\"manual\"]}";
    }

    public static String passRedemptionResult() {
        return "{\"passId\":\"pass-1\",\"passNumber\":\"LP-001\""
                + ",\"redeemedAt\":\"2026-01-01T12:00:00Z\",\"redemptionMethod\":\"qr_code\""
                + ",\"previousStatus\":\"active\",\"newStatus\":\"redeemed\",\"alreadyRedeemed\":false}";
    }

    public static String globalPassDto() {
        return "{\"id\":\"pass-1\",\"serialNumber\":\"SN-001\",\"platform\":\"apple\""
                + ",\"status\":\"active\",\"generatedAt\":\"2026-01-01T00:00:00Z\""
                + ",\"holderEmail\":\"jane@example.com\",\"templateId\":\"tpl-1\""
                + ",\"templateName\":\"Event Pass\",\"templateType\":\"event\"}";
    }
}
