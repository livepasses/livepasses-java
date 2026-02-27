package com.livepasses.sdk.examples;

import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;

import java.util.List;

/**
 * Coupon pass generation and redemption.
 *
 * Run: export LIVEPASSES_API_KEY="your-api-key"
 *      ./gradlew run -PmainClass=com.livepasses.sdk.examples.CouponWorkflowExample
 */
public class CouponWorkflowExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("LIVEPASSES_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Set LIVEPASSES_API_KEY environment variable");
            return;
        }

        Livepasses client = new Livepasses(apiKey);

        try {
            // 1. Generate coupon passes for a campaign
            System.out.println("Generating coupon passes...");
            String templateId = System.getenv("COUPON_TEMPLATE_ID");
            if (templateId == null) templateId = "coupon-template-id";

            PassGenerationResult result = client.passes().generateAndWait(
                    GeneratePassesParams.builder()
                            .templateId(templateId)
                            .businessContext(BusinessContext.builder()
                                    .coupon(CouponContext.builder()
                                            .campaignName("Summer Sale 2026")
                                            .specialMessage("Enjoy 20% off your next purchase!")
                                            .promotionStartDate("2026-06-01")
                                            .promotionEndDate("2026-08-31")
                                            .build())
                                    .build())
                            .passes(List.of(
                                    PassRecipient.builder()
                                            .customer(CustomerInfo.builder()
                                                    .firstName("Maria").lastName("Garcia")
                                                    .email("maria@example.com").build())
                                            .businessData(BusinessData.builder()
                                                    .promoCode("SUMMER20-001").maxUsageCount(1).build())
                                            .build(),
                                    PassRecipient.builder()
                                            .customer(CustomerInfo.builder()
                                                    .firstName("Pedro").lastName("Lopez")
                                                    .email("pedro@example.com").build())
                                            .businessData(BusinessData.builder()
                                                    .promoCode("SUMMER20-002").maxUsageCount(1).build())
                                            .build()
                            ))
                            .options(PassGenerationOptions.builder()
                                    .deliveryMethod("email").build())
                            .build(),
                    GenerateAndWaitOptions.builder()
                            .onProgress(s -> System.out.printf("  Progress: %.0f%%%n", s.getProgressPercentage()))
                            .build()
            );

            System.out.printf("Generated %d coupon(s)%n%n", result.getTotalPasses());

            // 2. Look up a coupon
            String passId = result.getPasses().get(0).getId();
            System.out.println("Looking up coupon...");
            PassLookupResult lookup = client.passes().lookup(
                    LookupPassParams.builder().passId(passId).build());
            System.out.printf("  Holder: %s%n  Status: %s%n%n", lookup.getHolderName(), lookup.getStatus());

            // 3. Validate before redemption
            System.out.println("Validating coupon...");
            PassValidationResult validation = client.passes().validate(passId);
            System.out.printf("  Can redeem: %s%n%n", validation.isCanBeRedeemed());

            // 4. Redeem the coupon at a store
            if (validation.isCanBeRedeemed()) {
                System.out.println("Redeeming coupon...");
                try {
                    PassRedemptionResult redemption = client.passes().redeemCoupon(passId,
                            RedeemCouponParams.builder()
                                    .location(RedemptionLocation.builder()
                                            .name("Store #42").latitude(4.6097).longitude(-74.0817).build())
                                    .notes("Applied to order #12345")
                                    .build());
                    System.out.printf("  Previous: %s%n  New: %s%n  At: %s%n%n",
                            redemption.getPreviousStatus(), redemption.getNewStatus(),
                            redemption.getRedeemedAt());
                } catch (BusinessRuleException e) {
                    System.err.println("  Cannot redeem: " + e.getMessage());
                }
            }

            System.out.println("Done!");

        } catch (LivepassesException e) {
            System.err.printf("API error [%s]: %s%n", e.getCode(), e.getMessage());
        }
    }
}
