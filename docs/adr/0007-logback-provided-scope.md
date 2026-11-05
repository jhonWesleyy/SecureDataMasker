# ADR-007: Logback in `provided` scope; minimum version 1.5.19

- **Status:** Accepted, with one open question (the Logback 1.6 line; see below)
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
3. The build compiles against the minimum by default (`logback.version` = 1.5.19). Other versions
   are tested with `-Dlogback.version=...`. The CI matrix "minimum × latest" is Phase 3.
4. Phase 5 must register `%mask` through `PatternLayoutBase.getInstanceConverterMap()`, which
   exists unchanged in 1.5.19 and 1.6.6. It must not use `getDefaultConverterMap()` or the static
   `PatternLayout.defaultConverterMap`, because 1.6.0 removed them (findings, section 1.4).

### Open question: Logback 1.6.x

The spec (RNF08 and section 10.1) says "Logback 1.5.x" and calls 1.5 "the active series". As of
2026-11-05, Maven Central lists 1.6.0 to 1.6.6 as well. 1.6.0 was released on 2026-07-23 and
removed deprecated APIs. The maintainer decided to **keep RNF08 unchanged** in Phase 2. The PoC
nevertheless covers 1.6.6, where it passes. Whether to declare 1.6.x supported, and whether 1.5.x
stays in the matrix, is left to the maintainer. It is recorded in `docs/phase-2-findings.md`,
section 5.

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
