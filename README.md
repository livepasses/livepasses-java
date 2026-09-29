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

// Coupon redemption with free-form context (metadata)
PassRedemptionResult result = client.passes().redeemCoupon("pass-id",
    RedeemCouponParams.builder()
        .location(RedemptionLocation.builder().name("Store #42").build())
        .metadata(Map.of("orderId", "12345"))
        .build());
```

### Update a pass

Change fields on an existing pass and, optionally, notify the holder. `updatedFields` keys are
updatable field names such as `validUntil`, `memberTier` or `points`; a request needs at least one
updated field or a `messageBody`. Pass `notify(false)` to update silently.

```java
client.passes().update("pass-id",
    UpdatePassParams.builder()
        .updatedField("memberTier", "Platinum")
        .reason("Reached 750 points")
        .messageHeader("Welcome to Platinum")
        .messageBody("Congratulations on reaching Platinum!")
        .build());
```

### Push a scoped update

Push field updates to all eligible passes of a template:

```java
client.passes().pushTemplate("template-id",
    PushTemplatePassesParams.builder()
        .updatedFields(Map.of("gate", "Gate C"))
        .reason("Event-wide gate change")
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
                    // Identifies the member — use a distinct number per person.
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
        // The block you send decides the template type: "event" makes an event ticket.
        .businessFeatures(Map.of(
            "event", Map.of(
                "eventName", "Aurora Music Fest",
                "eventDate", "2030-06-15T20:00:00Z",
                "venueName", "Aurora Arena",
                "showSeatNumbers", true)
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

The API answers every refusal with a real HTTP status — `400`, `403`, `404`, `409`, `422`, `429`,
`500`, `502` or `503` — and the body is always the envelope
`{success:false,data:null,error:{code,message,details,timestamp,traceId,fields?}}`. The two
exceptions are a challenge `401` (empty body) and a proxy error (which may not be JSON at all).

The SDK raises a typed exception from **any** non-2xx response, or from a parsed envelope with
`success:false` — this applies to every call, including paged list calls. An empty or non-JSON
body still raises a typed exception, built from the HTTP status alone. Classification checks
`error.code` first and falls back to the HTTP status only when the code is unrecognized or
absent; the message and code themselves fall back (to a generic message, and to
`GENERAL_ERROR`) only when the field is `null`.

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

| Exception | Typical status | When |
|-----------|------------|------|
| `AuthenticationException` | 401 | Invalid, expired, or revoked API key |
| `ValidationException` | 400 | Request validation failed. Carries `getFields()` (`Map<String, List<String>>`, field name \| validation messages) when the API's `error.code` is `VALIDATION_ERROR` |
| `ForbiddenException` | 403 | Insufficient permissions |
| `NotFoundException` | 404 | Resource not found |
| `RateLimitException` | 429 | Rate limit exceeded |
| `QuotaExceededException` | 422 | API quota or subscription limit exceeded |

The status column is the one each class usually carries; the error's `getStatus()` is always the response's real HTTP status. A `401` is always the authentication error and a `403` always the forbidden error, whatever `error.code` says. A `409` without a mapped code, and every `5xx`, raise the base `LivepassesException`.
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
- **5xx Server Errors** — exponential backoff with jitter, and **only for idempotent HTTP
  methods** (`GET`, `HEAD`, `PUT`, `DELETE`). A `POST` that hits a `5xx` is never retried,
  because no SDK request carries an `Idempotency-Key` and retrying it could double-execute the
  operation (for example, generating passes twice).

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
./gradlew run -PmainClass=com.livepasses.sdk.examples.GeneratePassesExample
```

## Building from Source

```bash
./gradlew build    # compile + test
./gradlew test     # run tests only
./gradlew jar      # produce JAR
```

## License

MIT
