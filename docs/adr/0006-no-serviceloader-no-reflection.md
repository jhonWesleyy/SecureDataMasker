# ADR-006: No `ServiceLoader`, no reflection, and no public detector extension point in 0.x

- **Status:** Accepted
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

The library runs inside every application that logs, and its behavior should be visible in code
and in `logback.xml`. Discovery by `ServiceLoader` means that a jar on the classpath can change
what gets masked. That makes the masking implicit and adds a supply-chain path (threat T11).
Reflection would also need `opens` in `module-info`, and it breaks under `jlink` and native images.

RF15 ("custom detectors registered by API, no `ServiceLoader` in the MVP") is a *should*. The
planning sketch had a public `SensitiveDataDetector` interface and `TextMasker.Builder.detector(...)`.
Review found a design flaw in it. `Match.type()` is the closed enum `SensitiveDataType`, so a
custom detector could not report a new data type, and it could not have a masking policy of its
own (`MaskingPolicy` is chosen per `SensitiveDataType`). The extension point would ship
non-functional. Removing it after 1.0 would break compatibility (CLAUDE.md rule 14, D11).

## Decision

1. **No `ServiceLoader` and no reflection** anywhere in `core` or `logback`. Everything is wired
   explicitly through `TextMasker.builder()` or the encoder's XML setters. Logback's own
   configurator instantiates the two public Logback classes, which is how Logback itself works.
   That needs only `exports`, not `opens`.
2. **RF15 is deferred.** The 0.x public API has no `SensitiveDataDetector` interface and no
   `Builder.detector(...)`. The set of detectable types is the closed `SensitiveDataType` enum.
3. **Recorded for later: an open type model.** A future extension point needs, at minimum:
   - a type identity that is not a closed enum: for example a `SensitiveDataType` interface, or a
     final class with the built-in types as constants and custom types created by name;
   - a way to attach a `MaskingPolicy` (or a masking function) to a custom type;
   - a detector contract that keeps O(n) and fail-closed guarantees: a bounded look-ahead
     parameter, no allocation on the fast path, and exceptions converted to the fail-closed marker;
   - overlap priority for custom types relative to the built-in ones.

   Turning the enum into such a type is a breaking change. So the decision must be made **before
   1.0**, in its own ADR. The maintainer confirmed on 2026-11-05 that RF15 stays out of the MVP
   and that keeping or cutting it is decided before 1.0.

## Consequences

- What is masked is fully determined by code and configuration that the application owns.
- No `opens` in `module-info`; compatible with `jlink`.
- Users cannot add a type such as credit-card numbers in 0.x. They can mask other content with
  Logback's `%replace` alongside this library, as before.
- Keeping `SensitiveDataType` as an enum for now keeps `switch` exhaustiveness. Changing it later
  is the cost of deferring, and it is accepted while the series is 0.x (D11).

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| `ServiceLoader` discovery of detectors | Implicit behavior; any jar can change masking; T11 |
| Ship `SensitiveDataDetector` now, fix the type model later | Ships an extension point that cannot work, then breaks it |
| Make `SensitiveDataType` open now | Designs an extension mechanism without a requirement or a user (rule 2); can be done before 1.0 if RF15 is confirmed |
| Configure detectors by class name in `logback.xml` | Needs reflection and `opens`; same implicitness problem |
