# ADR-007: Logback in `provided` scope; supported range 1.5.19+ including 1.6.x

- **Status:** Accepted (the Logback 1.6 question was resolved by the maintainer on 2026-11-05)
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

Applications, Spring Boot in particular, already choose their Logback version. If the library
pulled in its own version, it could downgrade a security fix or conflict with the application's
version. Logback before 1.5.19 is affected by CVE-2025-11226 (spec section 8.2).

## Decision

1. `securedatamasker-logback` declares `ch.qos.logback:logback-classic` with scope **`provided`**.
   The application supplies Logback, and the library never bundles or shades it.
2. **Minimum supported version: 1.5.19.** The evidence is in `docs/phase-2-findings.md`, section 1:
   - The Phase 2 PoC passes unchanged on 1.5.18, 1.5.19, 1.5.38 and 1.6.6. Logback's behavior is
     therefore not what sets the minimum.
   - 1.5.19 is the first 1.5.x release with the CVE-2025-11226 fix (GitLab advisory). Supporting
     anything older would mean documenting a vulnerable baseline.
   - The skeleton compiles against 1.5.19, 1.5.38 and 1.6.6.
3. **Supported range: 1.5.19 and later, including the 1.6.x line** (maintainer decision,
   2026-11-05). The build compiles against the minimum by default (`logback.version` = 1.5.19).
   Other versions are tested with `-Dlogback.version=...`. The Phase 3 CI matrix tests three
   Logback versions: **1.5.19** (minimum), **the latest 1.5.x** and **the latest 1.6.x**.
4. Phase 5 must register `%mask` through `PatternLayoutBase.getInstanceConverterMap()`, which
   exists unchanged in 1.5.19 and 1.6.6. It must not use `getDefaultConverterMap()` or the static
   `PatternLayout.defaultConverterMap`, because 1.6.0 removed them (findings, section 1.4).

### Resolved: Logback 1.6.x

The spec (RNF08 and section 10.1) said "Logback 1.5.x" and called 1.5 "the active series". As of
2026-11-05, Maven Central also lists 1.6.0 to 1.6.6; 1.6.0 was released on 2026-07-23 and removed
deprecated APIs. The PoC and the skeleton pass on 1.6.6 (findings, section 1). The maintainer
decided to support 1.5.19+ including 1.6.x and to keep a 1.5.x leg in the CI matrix, because
applications will stay on 1.5 for some time. The spec was updated accordingly (RNF08, sections
10.1, 14.2 and 16).

## Consequences

- No version conflicts and no forced downgrades for consumers.
- The library depends on a Logback API surface the consumer controls. The CI matrix and the PoC
  guard against drift across versions.
- Users on a Logback older than 1.5.19 are unsupported. The documentation will say so with the
  reason.

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| `compile` scope | Imposes a version; risks downgrading the application's Logback |
| `optional` dependency | Meant for optional features of a library that works without them; the `logback` module cannot work without Logback, so `provided` states the intent (supplied by the runtime) |
| Shading Logback | Duplicates classes; defeats the consumer's patching |
| Minimum 1.5.18 or lower (the PoC passes there) | Would document a version with a known CVE as supported |
