package com.livepasses.sdk.examples;

import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;

import java.util.List;

/**
 * Loyalty card lifecycle: generate, earn points, spend points, update tier.
 *
 * Run: export LIVEPASSES_API_KEY="your-api-key"
 *      ./gradlew run -PmainClass=com.livepasses.sdk.examples.LoyaltyWorkflowExample
 */
public class LoyaltyWorkflowExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("LIVEPASSES_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Set LIVEPASSES_API_KEY environment variable");
            return;
        }

        Livepasses client = new Livepasses(apiKey);

        try {
            // 1. Generate a loyalty card
            System.out.println("Generating loyalty card...");
            String templateId = System.getenv("LOYALTY_TEMPLATE_ID");
            if (templateId == null) templateId = "loyalty-template-id";

            PassGenerationResult result = client.passes().generateAndWait(
                    GeneratePassesParams.builder()
                            .templateId(templateId)
                            .passes(List.of(
                                    PassRecipient.builder()
                                            .customer(CustomerInfo.builder()
                                                    .firstName("Carlos").lastName("Rivera")
                                                    .email("carlos@example.com").phone("+57300123456").build())
                                            .businessData(BusinessData.builder()
                                                    .membershipNumber("MEM-2026-001")
                                                    .currentPoints(0).memberTier("Bronze")
                                                    .accountBalance(0.0).build())
                                            .build()
                            ))
                            .build(),
                    GenerateAndWaitOptions.builder()
                            .onProgress(s -> System.out.printf("  Progress: %.0f%%%n", s.getProgressPercentage()))
                            .build()
            );

            String passId = result.getPasses().get(0).getId();
            System.out.printf("  Pass ID: %s%n%n", passId);

            // 2. Earn points from a purchase
            System.out.println("Earning 500 points from purchase...");
            client.passes().loyaltyTransact(passId,
                    LoyaltyTransactionParams.builder()
                            .transactionType("earn").points(500)
                            .description("Purchase at Store #42 - $50.00").build());

            // 3. Earn more points
            System.out.println("Earning 300 more points...");
            client.passes().loyaltyTransact(passId,
                    LoyaltyTransactionParams.builder()
                            .transactionType("earn").points(300)
                            .description("Purchase at Store #15 - $30.00").build());

            // 4. Spend points for a reward
            System.out.println("Spending 200 points on a reward...");
            PassRedemptionResult spend = client.passes().loyaltyTransact(passId,
                    LoyaltyTransactionParams.builder()
                            .transactionType("spend").points(200)
                            .description("Redeemed: Free coffee").build());
            System.out.printf("  Status: %s%n%n", spend.getNewStatus());

            // 5. Update tier based on accumulated points
            System.out.println("Upgrading to Gold tier...");
            client.passes().update(passId,
                    UpdatePassParams.builder()
                            .businessData(BusinessData.builder()
                                    .currentPoints(600).memberTier("Gold").build())
                            .businessContext(BusinessContext.builder()
                                    .loyalty(LoyaltyContext.builder()
                                            .programUpdate("Congratulations! You've been upgraded to Gold tier!")
                                            .seasonalMessage("Enjoy double points this month!")
                                            .build())
                                    .build())
                            .build());
            System.out.println("  Tier updated to Gold\n");

            // 6. Validate the pass
            System.out.println("Validating pass...");
            PassValidationResult validation = client.passes().validate(passId);
            System.out.printf("  Valid: %s%n  Message: %s%n%n",
                    validation.isCanBeRedeemed(), validation.getValidationMessage());

            System.out.println("Done!");

        } catch (BusinessRuleException e) {
            System.err.println("Business rule violation: " + e.getMessage());
        } catch (LivepassesException e) {
            System.err.printf("API error [%s]: %s%n", e.getCode(), e.getMessage());
        }
    }
}
