package com.livepasses.sdk.examples;

import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;

import java.util.List;

/**
 * Complete usage example: Generate passes, look up, validate, and check in.
 *
 * Run: export LIVEPASSES_API_KEY="your-api-key"
 *      ./gradlew run -PmainClass=com.livepasses.sdk.examples.GeneratePassesExample
 */
public class GeneratePassesExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("LIVEPASSES_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Set LIVEPASSES_API_KEY environment variable");
            System.out.println("Get your key at https://dashboard.livepasses.com/api-keys");
            return;
        }

        Livepasses client = new Livepasses(apiKey);

        // 1. List templates
        System.out.println("=== Templates ===");
        PagedResponse<TemplateListItem> templates = client.templates().list(null);
        for (TemplateListItem t : templates.getItems()) {
            System.out.printf("  %s: %s (%s)%n", t.getId(), t.getName(), t.getType());
        }

        if (templates.getItems().isEmpty()) {
            System.out.println("No templates found. Create one first.");
            return;
        }

        String templateId = templates.getItems().get(0).getId();

        // 2. Generate event pass with auto-polling
        System.out.println("\n=== Generate Pass ===");
        try {
            PassGenerationResult result = client.passes().generateAndWait(
                    GeneratePassesParams.builder()
                            .templateId(templateId)
                            .businessContext(BusinessContext.builder()
                                    .event(EventContext.builder()
                                            .eventName("Tech Conference 2026")
                                            .eventDate("2026-06-15T09:00:00Z")
                                            .build())
                                    .build())
                            .passes(List.of(
                                    PassRecipient.builder()
                                            .customer(CustomerInfo.builder()
                                                    .firstName("Alice").lastName("Smith")
                                                    .email("alice@example.com").build())
                                            .businessData(BusinessData.builder()
                                                    .sectionInfo("VIP").rowInfo("A").seatNumber("1")
                                                    .ticketType("VIP").price(150.0).currency("USD")
                                                    .build())
                                            .build()
                            ))
                            .options(PassGenerationOptions.builder()
                                    .deliveryMethod("email").build())
                            .build(),
                    GenerateAndWaitOptions.builder()
                            .onProgress(status -> System.out.printf(
                                    "  Progress: %.0f%%%n", status.getProgressPercentage()))
                            .build()
            );

            System.out.printf("Generated %d pass(es)%n", result.getTotalPasses());
            for (GeneratedPass pass : result.getPasses()) {
                System.out.printf("  Pass %s: Apple=%s, Google=%s%n",
                        pass.getId(),
                        pass.getPlatforms().getApple().isAvailable(),
                        pass.getPlatforms().getGoogle().isAvailable());
            }

            // 3. Validate
            if (!result.getPasses().isEmpty()) {
                String passId = result.getPasses().get(0).getId();
                PassValidationResult validation = client.passes().validate(passId);
                System.out.printf("\nValidation: canRedeem=%s, methods=%s%n",
                        validation.isCanBeRedeemed(), validation.getVerificationMethods());

                // 4. Check in
                if (validation.isCanBeRedeemed()) {
                    PassRedemptionResult redemption = client.passes().checkIn(passId,
                            CheckInParams.builder()
                                    .location(RedemptionLocation.builder()
                                            .name("Main Gate").latitude(40.71).longitude(-74.00).build())
                                    .build());
                    System.out.printf("Check-in: %s -> %s%n",
                            redemption.getPreviousStatus(), redemption.getNewStatus());
                }
            }
        } catch (AuthenticationException e) {
            System.err.println("Invalid API key: " + e.getMessage());
        } catch (ValidationException e) {
            System.err.println("Validation error: " + e.getMessage() + " (" + e.getDetails() + ")");
        } catch (RateLimitException e) {
            System.err.printf("Rate limited. Retry after %ds%n", e.getRetryAfter());
        } catch (QuotaExceededException e) {
            System.err.println("Quota exceeded: " + e.getMessage());
        } catch (NotFoundException e) {
            System.err.println("Not found: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.err.println("Business rule: " + e.getMessage());
        }
    }
}
