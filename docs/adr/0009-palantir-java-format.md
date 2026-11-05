# ADR-009: Spotless with palantir-java-format

- **Status:** Proposed (Phase 3; approved with the Phase 3 gate)
- **Date:** 2026-11-05
- **Phase:** 3 (Foundation)

## Context

Spec section 10.3 selects Spotless to enforce formatting, with "google-java-format (or palantir)",
and drops Checkstyle: formatting is fixed by a formatter, not by rules. The choice was left to
Phase 3. The Phase 2 code was written by hand in a 4-space style with 8-space continuation
indentation and lines of up to about 100 characters. `.editorconfig` records 4 spaces for Java.

Both candidates were run with Spotless 3.11.0 over the Phase 2 sources, and the size of the change
each one would make was measured:

| Formatter | Style | Files changed | Lines (+/−) | What changes |
| --- | --- | --- | --- | --- |
| palantir-java-format 2.103.0 | `PALANTIR` (4 spaces, 120 columns) | 3 (2 of them tests) | +17 / −21 | Short call chains and argument lists joined onto one line; `@Serial` moved to its own line |
| google-java-format 2.0.0 | `AOSP` (4 spaces, 100 columns) | 18 | +79 / −61 | Javadoc reflowed in most public types, including `{@link ...}` split across lines |
| google-java-format 2.0.0 | `GOOGLE` (default) | not measured | — | 2-space indentation: rewrites every line, contradicts the existing style |

## Decision

Use **Spotless with palantir-java-format, style `PALANTIR`**, plus `removeUnusedImports`, on Java
sources only. `spotless:check` is bound to `verify`, so an unformatted file fails the build and
CI; `./mvnw spotless:apply` fixes it. Javadoc formatting is left to the author (palantir does not
reflow comments by default).

Reasons:

- It keeps the indentation already used (4 spaces, 8 for continuations) without a non-default
  mode.
- It produced the smallest diff on the existing code, and none in Javadoc. The API Javadoc was
  reviewed at the Phase 2 gate; a formatter that rewraps it would make that review harder to trace.
- It is actively released and runs on JDK 21+ (the Maven JVM in this project is JDK 25).

The formatting was applied in its own commit, separate from every other change.

## Consequences

- Lines may reach 120 characters, longer than the hand-written 100. Existing Javadoc keeps its
  shorter wrapping; new code follows the formatter.
- Formatting is a build gate. Contributors run `./mvnw spotless:apply` before committing
  (CONTRIBUTING.md).
- Palantir, like google-java-format, uses internal `javac` APIs. Formatter updates arrive through
  Dependabot and may reformat code; such a bump must be merged together with its reformatting.

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| google-java-format, `AOSP` style | Larger diff; reflows Javadoc already reviewed at the Phase 2 gate |
| google-java-format, default style | 2-space indentation, contradicting the existing code and `.editorconfig` |
| Checkstyle (style rules) | Rejected in spec 10.3: reports problems instead of fixing them, redundant with a formatter |
| No formatter, `.editorconfig` only | Not enforceable in CI |
