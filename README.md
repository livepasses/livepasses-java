# Livepasses Java SDK

Official Java SDK for the [Livepasses API](https://livepasses.com) - generate, manage, and redeem Apple Wallet and Google Wallet passes.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.livepasses:livepasses-java:0.1.0")
```

### Gradle (Groovy)

```groovy
implementation 'com.livepasses:livepasses-java:0.1.0'
```

### Maven

```xml
<dependency>
    <groupId>com.livepasses</groupId>
    <artifactId>livepasses-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

Requires **Java 11+**. Single runtime dependency: Jackson (`jackson-databind`).

## Quick Start

```java
import com.livepasses.sdk.Livepasses;
import com.livepasses.sdk.types.*;

Livepasses client = new Livepasses("lp_api_key_...");

PassGenerationResult result = client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("your-template-id")
        .passes(List.of(
            PassRecipient.builder()
                .customer(CustomerInfo.builder()
                    .firstName("Jane").lastName("Doe").email("jane@example.com").build())
                .businessData(BusinessData.builder()
                    .sectionInfo("A").rowInfo("12").seatNumber("5").build())
                .build()
        ))
        .build()
);

System.out.println(result.getPasses().get(0).getPlatforms().getApple().getAddToWalletUrl());
```

## Authentication

Pass your API key when creating the client. Get your key at [dashboard.livepasses.com/api-keys](https://dashboard.livepasses.com/api-keys).

```java
Livepasses client = new Livepasses("lp_api_key_...",
    LivepassesOptions.builder()
        .baseUrl("https://api.livepasses.com")  // default
        .timeout(Duration.ofSeconds(30))          // default
        .maxRetries(3)                            // default
        .build());
```

## Pass Generation

### Single pass (synchronous)

```java
PassGenerationResult result = client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("template-id")
        .businessContext(BusinessContext.builder()
            .event(EventContext.builder()
                .eventName("Summer Concert 2026")
                .eventDate("2026-07-15T18:00:00Z")
                .build())
            .build())
        .passes(List.of(
            PassRecipient.builder()
                .customer(CustomerInfo.builder()
                    .firstName("Jane").lastName("Doe").email("jane@example.com").build())
                .businessData(BusinessData.builder()
                    .sectionInfo("VIP").rowInfo("A").seatNumber("12")
                    .ticketType("VIP").price(150.0).currency("USD")
                    .build())
                .build()
        ))
        .options(PassGenerationOptions.builder().deliveryMethod("email").build())
        .build()
);
```

### Batch passes (auto-polling)

For multiple recipients, `generateAndWait` automatically polls until the batch completes:

```java
PassGenerationResult result = client.passes().generateAndWait(
    GeneratePassesParams.builder()
        .templateId("template-id")
        .passes(recipients)
        .build(),
    GenerateAndWaitOptions.builder()
        .pollIntervalMs(2000)  // 2s between polls (default)
        .onProgress(status ->
            System.out.printf("%.0f%% complete%n", status.getProgressPercentage()))
        .build()
);
```

### Batch status (manual polling)

If you need more control over polling, use `generate` + `getBatchStatus`:

```java
PassGenerationResult initial = client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("template-id")
        .passes(recipients)
        .build()
);

if (initial.getBatchOperation() != null) {
    String batchId = initial.getBatchOperation().getBatchId();
    BatchStatusResult status = client.passes().getBatchStatus(batchId);
    while (!status.isCompleted()) {
        Thread.sleep(2000);
        status = client.passes().getBatchStatus(batchId);
        System.out.printf("Progress: %.0f%%%n", status.getProgressPercentage());
    }
    System.out.printf("Completed: %d successful, %d failed%n",
        status.getStatistics().getSuccessful(), status.getStatistics().getFailed());
}
```

## Pass Lifecycle

### Lookup

```java
PassLookupResult pass = client.passes().lookup(
    LookupPassParams.builder().passId("pass-id").build());
// or by pass number
PassLookupResult pass = client.passes().lookup(
    LookupPassParams.builder().passNumber("LP-001").build());
```

### Validate

```java
PassValidationResult validation = client.passes().validate("pass-id");
if (validation.isCanBeRedeemed()) {
    // proceed with redemption
}
```

### Redeem

```java
// Generic redemption
PassRedemptionResult result = client.passes().redeem("pass-id", null);

// Event check-in with location
PassRedemptionResult result = client.passes().checkIn("pass-id",
    CheckInParams.builder()
        .location(RedemptionLocation.builder()
            .name("Gate 1").latitude(40.71).longitude(-74.00).build())
        .build());

// Coupon redemption with notes
PassRedemptionResult result = client.passes().redeemCoupon("pass-id",
    RedeemCouponParams.builder()
        .location(RedemptionLocation.builder().name("Store #42").build())
        .notes("Applied to order #12345")
        .build());
```

### Update a pass

Update business data or context on an existing pass:

```java
client.passes().update("pass-id",
    UpdatePassParams.builder()
        .businessData(BusinessData.builder()
            .currentPoints(750).memberTier("Platinum").build())
        .businessContext(BusinessContext.builder()
            .loyalty(LoyaltyContext.builder()
                .programUpdate("Congratulations on reaching Platinum!").build())
            .build())
        .build());
```

### Bulk update

Update multiple passes at once:

```java
client.passes().bulkUpdate(
    BulkUpdatePassesParams.builder()
        .passIds(List.of("pass-1", "pass-2", "pass-3"))
        .businessData(BusinessData.builder().memberTier("Gold").build())
        .businessContext(BusinessContext.builder()
            .loyalty(LoyaltyContext.builder()
                .seasonalMessage("Happy holidays from our team!").build())
            .build())
        .build());
```

## Pass Types

### Event Passes

```java
client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("event-template-id")
        .businessContext(BusinessContext.builder()
            .event(EventContext.builder()
                .eventName("Concert").eventDate("2026-07-15T18:00:00Z")
                .doorsOpen("2026-07-15T16:00:00Z").build())
            .build())
        .passes(List.of(
            PassRecipient.builder()
                .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                .businessData(BusinessData.builder()
                    .sectionInfo("A").rowInfo("1").seatNumber("5").gateInfo("North").build())
                .build()
        ))
        .build()
);
```

### Loyalty Cards

```java
client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("loyalty-template-id")
        .passes(List.of(
            PassRecipient.builder()
                .customer(CustomerInfo.builder()
                    .firstName("Jane").lastName("Doe").email("jane@example.com").build())
                .businessData(BusinessData.builder()
                    .membershipNumber("MEM-001").currentPoints(500).memberTier("Gold").build())
                .build()
        ))
        .build()
);

// Earn points
client.passes().loyaltyTransact("pass-id",
    LoyaltyTransactionParams.builder()
        .transactionType("earn").points(100).description("Purchase reward").build());

// Spend points
client.passes().loyaltyTransact("pass-id",
    LoyaltyTransactionParams.builder()
        .transactionType("spend").points(50).description("Redeemed: Free coffee").build());
```

### Coupon Passes

```java
client.passes().generate(
    GeneratePassesParams.builder()
        .templateId("coupon-template-id")
        .businessContext(BusinessContext.builder()
            .coupon(CouponContext.builder()
                .campaignName("Summer Sale").specialMessage("20% off!")
                .promotionStartDate("2026-06-01").promotionEndDate("2026-08-31").build())
            .build())
        .passes(List.of(
            PassRecipient.builder()
                .customer(CustomerInfo.builder().firstName("Jane").lastName("Doe").build())
                .businessData(BusinessData.builder()
                    .promoCode("SUMMER20").maxUsageCount(1).build())
                .build()
        ))
        .build()
);
```

## Templates

### List and get

```java
// List templates
PagedResponse<TemplateListItem> templates = client.templates().list(
    ListTemplatesParams.builder().status("Active").build());

// Get template details
TemplateDetail template = client.templates().get("template-id");
```

### Create a template

```java
TemplateDetail template = client.templates().create(
    CreateTemplateParams.builder()
        .name("VIP Event Pass")
        .description("Premium event ticket template")
        .businessFeatures(Map.of(
            "passType", "event",
            "hasSeating", true,
            "supportedPlatforms", List.of("apple", "google")
        ))
        .build());
System.out.printf("Created: %s — %s%n", template.getId(), template.getName());
```

### Update a template

```java
TemplateDetail updated = client.templates().update("template-id",
    UpdateTemplateParams.builder()
        .name("VIP Event Pass v2")
        .description("Updated premium event ticket template")
        .build());
```

### Activate / deactivate

```java
client.templates().activate("template-id");
client.templates().deactivate("template-id");
```

## Webhooks

```java
// Register a webhook
Webhook webhook = client.webhooks().create(
    CreateWebhookParams.builder()
        .url("https://your-app.com/webhooks/livepasses")
        .events(List.of(
            WebhookEventType.PASS_GENERATED,
            WebhookEventType.PASS_REDEEMED,
            WebhookEventType.BATCH_COMPLETED))
        .build());
System.out.println("Secret: " + webhook.getSecret()); // use to verify signatures

// List webhooks
List<Webhook> webhooks = client.webhooks().list();

// Remove a webhook
client.webhooks().delete("webhook-id");
```

## Error Handling

All errors extend `LivepassesException` (unchecked) for precise catch handling:

```java
import com.livepasses.sdk.exceptions.*;

try {
    client.passes().generate(params);
} catch (AuthenticationException e) {
    System.err.println("Invalid API key");
} catch (ValidationException e) {
    System.err.println("Invalid input: " + e.getMessage() + " " + e.getDetails());
} catch (ForbiddenException e) {
    System.err.println("Insufficient permissions: " + e.getMessage());
} catch (RateLimitException e) {
    System.err.printf("Rate limited. Retry after %ds%n", e.getRetryAfter());
} catch (QuotaExceededException e) {
    System.err.println("Monthly pass quota exceeded — upgrade your plan");
} catch (NotFoundException e) {
    System.err.println("Template or pass not found");
} catch (BusinessRuleException e) {
    System.err.println("Business rule: " + e.getMessage());
} catch (LivepassesException e) {
    // Catch-all for any other API error
    System.err.printf("API error [%s]: %s (HTTP %d)%n", e.getCode(), e.getMessage(), e.getStatus());
}
```

### Error codes

Use `ApiErrorCodes` for programmatic error code comparisons:

```java
import com.livepasses.sdk.types.ApiErrorCodes;

try {
    client.passes().redeem("pass-id", null);
} catch (LivepassesException e) {
    switch (e.getCode()) {
        case ApiErrorCodes.PASS_ALREADY_USED:
            System.err.println("This pass has already been redeemed");
            break;
        case ApiErrorCodes.PASS_EXPIRED:
            System.err.println("This pass has expired");
            break;
        case ApiErrorCodes.TEMPLATE_INACTIVE:
            System.err.println("The template for this pass is inactive");
            break;
        default:
            System.err.println("Unhandled error: " + e.getCode());
    }
}
```

### Exception hierarchy

| Exception | HTTP Status | When |
|-----------|------------|------|
| `AuthenticationException` | 401 | Invalid, expired, or revoked API key |
| `ValidationException` | 400 | Request validation failed |
| `ForbiddenException` | 403 | Insufficient permissions |
| `NotFoundException` | 404 | Resource not found |
| `RateLimitException` | 429 | Rate limit exceeded |
| `QuotaExceededException` | 403 | API quota or subscription limit exceeded |
| `BusinessRuleException` | 422 | Business rule violation (pass expired, already used, etc.) |

## Pagination

### Manual pagination

```java
PagedResponse<GlobalPassDto> page1 = client.passes().list(
    ListPassesParams.builder().page(1).pageSize(50).build());
System.out.printf("Page 1 of %d (%d total)%n",
    page1.getPagination().getTotalPages(), page1.getPagination().getTotalItems());
```

### Auto-pagination

```java
Iterator<GlobalPassDto> it = client.passes().listAutoPaginate(
    ListPassesParams.builder().templateId("tpl-id").build());

while (it.hasNext()) {
    GlobalPassDto pass = it.next();
    System.out.println(pass.getId());
}

// Or use the Iterable helper for enhanced for-loops
for (GlobalPassDto pass : PaginationIterator.iterable(
        page -> client.passes().list(ListPassesParams.builder().page(page).build()))) {
    System.out.println(pass.getId());
}
```

## Configuration

| Option | Default | Description |
|--------|---------|-------------|
| `baseUrl` | `https://api.livepasses.com` | API base URL |
| `timeout` | `Duration.ofSeconds(30)` | Request timeout |
| `maxRetries` | `3` | Max retries for failed requests (429, 5xx) |

### Automatic retries

The SDK automatically retries on:
- **429 Too Many Requests** — honors `Retry-After` header
- **5xx Server Errors** — exponential backoff with jitter

## Examples

See the [`examples/`](./examples/) directory for runnable classes:

- **[GeneratePassesExample.java](./examples/GeneratePassesExample.java)** — End-to-end pass generation, lookup, validation, and check-in
- **[LoyaltyWorkflowExample.java](./examples/LoyaltyWorkflowExample.java)** — Loyalty card lifecycle: generate, earn points, spend points, update tier
- **[CouponWorkflowExample.java](./examples/CouponWorkflowExample.java)** — Coupon pass generation and redemption
- **[TemplateManagementExample.java](./examples/TemplateManagementExample.java)** — CRUD operations on pass templates
- **[WebhookSetupExample.java](./examples/WebhookSetupExample.java)** — Register, list, and manage webhooks

Run any example with:
```bash
export LIVEPASSES_API_KEY="your-api-key"
cd sdks/java
./gradlew run -PmainClass=com.livepasses.sdk.examples.GeneratePassesExample
```

## Building from Source

```bash
cd sdks/java
./gradlew build    # compile + test
./gradlew test     # run tests only
./gradlew jar      # produce JAR
```

## License

MIT
