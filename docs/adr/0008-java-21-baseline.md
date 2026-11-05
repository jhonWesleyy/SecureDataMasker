# ADR-008: Java 21 baseline, built with JDK 25 and `--release 21`

- **Status:** Accepted (D1, approved 2026-11-04)
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

The minimum Java version is part of the public contract. Raising it later is a breaking change
(D11). The 1.0 release is expected in 2027. The build also needs a recent JDK: `{@snippet}` in
Javadoc needs JDK 18 or later, and the static-analysis tools planned for Phase 3 (Error Prone,
NullAway in JSpecify mode) work best on the newest `javac`.

## Decision

- **Runtime baseline: Java 21.** The parent POM sets `maven.compiler.release=21`.
- **Build JDK: 25** (the current LTS). With `--release 21`, the compiler checks the code against
  the Java 21 API signatures (`ct.sym`) and rejects APIs added after 21, not only newer language
  features.
- **Test matrix: JDK 21 and 25** (CI is Phase 3). In Phase 2 the full build was run on both:
  - `mvn -B clean verify` on JDK 25.0.4.1;
  - `JAVA_HOME=…21 MAVEN_SKIP_RC=true MAVEN_OPTS= mvn -B clean verify` on JDK 21.0.12.1.

  Both pass (`docs/phase-2-findings.md`, section 1.5).
- Language features used by the API: `sealed` interfaces with records (`ValidationResult`),
  pattern matching for `switch` (Javadoc snippet), and `{@snippet}`.

### Why 17 was rejected

- **Support window.** Oracle's Premier Support for JDK 17 ended on 2026-09-30, and Eclipse Temurin
  17 runs until 2027-10-31. Temurin 21 runs until 2029-12-31 (spec section 10.1). A library whose
  1.0 ships in 2027 would commit to a baseline that leaves free vendor support within months.
- **Language.** Pattern matching for `switch` and record patterns are final only in Java 21
  (JEPs 441 and 440). They are what make the sealed `ValidationResult` and the per-type dispatch
  pleasant to use. On 17 they would be preview features, unusable in a library.
- **Tooling.** Error Prone 2.43.0 and later need JDK 21 or newer *to run*. NullAway's JSpecify mode
  needs JDK 22 or newer, or JDK 21.0.8+ with `-XDaddTypeAnnotationsToSymbol=true` (findings,
  section 2). Building on 25 meets both, whatever the baseline. A 17 baseline would gain nothing
  here, and a 17 *build* JDK would lose both tools.
- **Accepted cost.** Applications still on Java 17 cannot use the library. Java 17 users of
  Spring Boot 3 are the most visible group affected. This is the trade-off approved in D1.

## Consequences

- Contributors need JDK 25 to build as documented. The build also runs on JDK 21 (verified), so
  JDK 21 suffices for a quick local test. This environment detail matters for Phase 3:
  `MAVEN_OPTS=--sun-misc-unsafe-memory-access=allow` is set for JDK 25, and JDK 21 rejects that
  option (findings, section 1.5).
- Phase 3 decides how CI runs tests on 21 while compiling on 25 (toolchains are already
  provisioned in the development environment, but the plugin is deliberately not wired in Phase 2).
- Moving the baseline to 25 later would be a breaking change, allowed only in a major release
  after 1.0.

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| Java 17 baseline | See above |
| Java 25 baseline | Excludes most of the current market in 2026–2027; no API in the plan needs 25 |
| Build with JDK 21, no `--release` | Loses `javac` fixes that NullAway's JSpecify mode relies on; `-XDaddTypeAnnotationsToSymbol` only works on OpenJDK builds from 21.0.8 |
| Build with JDK 25 and `-source/-target 21` | Does not stop the use of APIs added after 21; `--release` does |
