# Changelog

All notable changes to the Livepasses Java SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.0] - 2026-09-28

### Added
- `PASS_INSTALLED` and `PASS_REMOVED` webhook events: the holder saved a pass to a wallet (first device), or removed its last copy. Holder-initiated removals only.
- Pass operations the API shipped since June: `passes().redeemGiftCard`, `passes().membershipCheckIn`, `passes().stamp`, `passes().unstamp`, `passes().redeemByScan`.
  `stamp` and `unstamp` send an empty JSON body rather than none: both endpoints bind a request
  DTO, and a bodyless POST carries no `Content-Type`, which the API answers with `415`.
- `ValidationException.getFields()` — a field name → validation message list, populated only when
  the API's `error.code` is `VALIDATION_ERROR`.

### Changed
- **BREAKING:** `UpdatePassParams` now matches the API: `updatedFields` (set with
  `updatedField(name, value)` or `updatedFields(map)`), `reason`, `messageHeader`, `messageBody`
  and `notify`. Its old `businessData` and `businessContext` were never read by
  `PUT /api/passes/{id}`, so `passes().update()` changed nothing while answering success; the API
  now refuses them with a `400`. `build()` throws `IllegalArgumentException` unless there is at
  least one updated field or a non-blank `messageBody`, which the API requires.
- **BREAKING:** `notes` is removed from `RedeemPassParams`, `CheckInParams` and
  `RedeemCouponParams`. No redeem endpoint ever read it, and the API now refuses it with a `400`.
  Use the new `metadata(Map<String, String>)` for free-form context such as an order number.
- **BREAKING:** The API now answers every refusal with a real HTTP status
  (`400`/`403`/`404`/`409`/`422`/`429`/`500`/`502`/`503`), always with the
  `{success:false,data:null,error:{...}}` envelope, instead of `200` with `success:false` for
  most refusals. The SDK now raises a typed exception from **any** non-2xx status or a parsed
  envelope with `success:false` — on every call path, including `getPaged`/paged list calls —
  not only when the body said `success:false`. An empty body (a challenge `401`) or a non-JSON
  body (a proxy error page) still raises a typed exception, built from the HTTP status alone;
  classification checks a `401`/`403` status first, then `error.code`, then the remaining
  statuses. If your code
  depended on a `200` response for a refusal, or on catching only exceptions built from a JSON
  `error` payload, it will now see a typed exception it didn't before.
- Automatic 5xx retries now apply **only to idempotent HTTP methods** (`GET`, `HEAD`, `PUT`,
  `DELETE`). A `POST` that hits a `5xx` is no longer retried — no SDK request carries an
  `Idempotency-Key`, so retrying a `POST` risked double-executing it. `429` retries are
  unaffected.
- Every typed exception's `getStatus()` is now the response's real HTTP status instead of a fixed
  number per class — `QuotaExceededException.getStatus()` is `422`, not `403`. A `401` is always an
  `AuthenticationException` and a `403` always a `ForbiddenException`, whatever the code: a `403`
  carrying `UNAUTHORIZED` is a permission refusal, not a bad API key. After that the error code
  decides, then the status (`400`/`404`/`422`/`429`); a `409` with no mapped code is a plain
  `LivepassesException`. Each exception gained a constructor overload that takes the status; the
  existing constructors are unchanged and delegate with the old fixed status.
- `ApiResponse.ApiError` keeps its three-argument constructor `(message, code, details)`, which
  now delegates to the four-argument one with `fields = null`. Jackson deserializes through the
  four-argument constructor, now marked `@JsonCreator`.
- **Upgrade recommended.** Older SDK versions retry a failed request on any `5xx`, including a `POST`. The API now answers server-side failures with a real `500`, `502` or `503` where it used to answer `200`, so an older SDK can send the same `POST` twice — for example, generate the same passes twice. This version retries a `5xx` only for `GET`, `HEAD`, `PUT` and `DELETE`.

### Removed
- **BREAKING:** `PASS_EXPIRED`, `PASS_CHECKED_IN`, `BATCH_COMPLETED` and `BATCH_FAILED` from `WebhookEventType`. The API rejects all four with a `400`, so no
  subscription using them could ever have worked.

### Fixed
- `redeem` documented itself as generic redemption. It is single-use only: multi-use passes
  are refused with a `422`. The javadoc now says so and names `stamp`, `membershipCheckIn`,
  `redeemCoupon` and `redeemGiftCard` as the operations to use instead.
- Webhook event catalogue now mirrors the server allow-list, adding `loyalty.transacted`,
  `coupon.applied`, the five `transfer.*` events and the `*` wildcard. The runnable webhook
  example no longer subscribes to events the API rejects.
- The template example and README put invented flat keys (`passType`, `hasSeating`,
  `hasGateInfo`, `supportedPlatforms`, `hasBackstageAccess`) in `businessFeatures`, which the
  API now refuses with a `400`. They now send a nested `event` block (and `branding`).

## [0.2.0] - 2026-05-23

### Changed
- **BREAKING:** `passes().bulkUpdate(BulkUpdatePassesParams)` replaced by `passes().pushTemplate(String templateId, PushTemplatePassesParams)`, targeting `POST /api/passes/template/{templateId}/push` with `{ updatedFields, reason }`. `BulkUpdatePassesParams` renamed to `PushTemplatePassesParams`.

## [0.1.0] - 2026-02-27

### Added

- Initial release of the Livepasses Java SDK
- `Livepasses` client with configurable base URL, timeout, and retry settings
- **Passes resource**: `generate`, `generateAndWait`, `list`, `listAutoPaginate`, `lookup`, `validate`, `update`, `bulkUpdate`, `redeem`, `checkIn`, `redeemCoupon`, `loyaltyTransact`, `getBatchStatus`
- **Templates resource**: `list`, `get`, `create`, `update`, `activate`, `deactivate`
- **Webhooks resource**: `create`, `list`, `delete`
- Exception hierarchy: `AuthenticationException`, `ValidationException`, `ForbiddenException`, `NotFoundException`, `RateLimitException`, `QuotaExceededException`, `BusinessRuleException`
- `ApiErrorCodes` constants with 40+ error code values
- Automatic retry with exponential backoff for 429 and 5xx responses
- Auto-pagination via `Iterator<T>` and `PaginationIterator.iterable()` helper
- Builder pattern for all request types
- Requires Java 11+, single runtime dependency (Jackson)

[0.1.0]: https://github.com/livepasses/livepasses-java/releases/tag/java-v0.1.0
