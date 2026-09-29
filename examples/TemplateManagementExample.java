package com.livepasses.sdk.examples;

import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.exceptions.*;
import com.livepasses.sdk.types.*;

import java.util.List;
import java.util.Map;

/**
 * CRUD operations on pass templates.
 *
 * Run: export LIVEPASSES_API_KEY="your-api-key"
 *      ./gradlew run -PmainClass=com.livepasses.sdk.examples.TemplateManagementExample
 */
public class TemplateManagementExample {

    public static void main(String[] args) {
        String apiKey = System.getenv("LIVEPASSES_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Set LIVEPASSES_API_KEY environment variable");
            return;
        }

        Livepasses client = new Livepasses(apiKey);

        try {
            // 1. Create a new event template
            System.out.println("Creating event template...");
            TemplateDetail template = client.templates().create(
                    CreateTemplateParams.builder()
                            .name("VIP Concert Pass")
                            .description("Premium concert ticket with VIP access")
                            // The template type is decided by which block is present:
                            // an "event" block makes an event ticket.
                            .businessFeatures(Map.of(
                                    "event", Map.of(
                                            "eventName", "Aurora Music Fest",
                                            "eventDate", "2030-06-15T20:00:00Z",
                                            "venueName", "Aurora Arena",
                                            "showSeatNumbers", true,
                                            "showGateInfo", true,
                                            "sectionTypes", List.of("VIP")),
                                    "branding", Map.of(
                                            "primaryColor", "#1A1A1D",
                                            "textColor", "#FFFFFF",
                                            "brandName", "AURORA FEST")
                            ))
                            .build());
            System.out.printf("  Created: %s — \"%s\"%n  Status: %s%n%n",
                    template.getId(), template.getName(), template.getStatus());

            // 2. Update the template
            System.out.println("Updating template...");
            TemplateDetail updated = client.templates().update(template.getId(),
                    UpdateTemplateParams.builder()
                            .name("VIP Concert Pass v2")
                            .description("Updated premium concert ticket with backstage access")
                            // PUT merges: send only what changed; omitted event fields keep their values.
                            .businessFeatures(Map.of(
                                    "event", Map.of(
                                            "sectionTypes", List.of("VIP", "Backstage"))
                            ))
                            .build());
            System.out.printf("  Updated: \"%s\"%n%n", updated.getName());

            // 3. Activate the template
            System.out.println("Activating template...");
            client.templates().activate(template.getId());
            System.out.println("  Template is now active\n");

            // 4. List all active templates
            System.out.println("Listing active templates...");
            PagedResponse<TemplateListItem> templates = client.templates().list(
                    ListTemplatesParams.builder().status("Active").build());
            for (TemplateListItem t : templates.getItems()) {
                System.out.printf("  - %s (%s) [%s]%n", t.getName(), t.getType(), t.getStatus());
            }
            System.out.printf("  Total: %d%n%n", templates.getPagination().getTotalItems());

            // 5. Get template details
            System.out.println("Getting template details...");
            TemplateDetail detail = client.templates().get(template.getId());
            System.out.printf("  Name: %s%n  Type: %s%n  Features: %s%n%n",
                    detail.getName(), detail.getType(), detail.getBusinessFeatures());

            // 6. Deactivate when done
            System.out.println("Deactivating template...");
            client.templates().deactivate(template.getId());
            System.out.println("  Template deactivated\n");

            System.out.println("Done!");

        } catch (ValidationException e) {
            System.err.println("Validation error: " + e.getMessage() + " (" + e.getDetails() + ")");
        } catch (LivepassesException e) {
            System.err.printf("API error [%s]: %s%n", e.getCode(), e.getMessage());
        }
    }
}
