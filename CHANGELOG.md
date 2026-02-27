# Changelog

All notable changes to the Livepasses Java SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
