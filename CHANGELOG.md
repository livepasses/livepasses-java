# Changelog

All notable changes to the Livepasses Java SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Pass operations the API shipped since June: `passes().redeemGiftCard`, `passes().membershipCheckIn`, `passes().stamp`, `passes().unstamp`, `passes().redeemByScan`.
  `stamp` and `unstamp` send an empty JSON body rather than none: both endpoints bind a request
  DTO, and a bodyless POST carries no `Content-Type`, which the API answers with `415`.

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
