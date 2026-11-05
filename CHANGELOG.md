# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html). In the `0.x` series the API
may change between minor versions; every such change is listed here (spec section 12).

## [Unreleased]

Nothing has been released yet. The public API is a skeleton: every method throws
`UnsupportedOperationException`.

### Added

- Phase 2 (Architecture): ADR-001 to ADR-008, the public API skeleton of `securedatamasker-core`
  and `securedatamasker-logback` with `module-info.java`, the masking policy table
  (`docs/masking-policy.md`) and a proof of concept of the implicit `%ex` bypass with plain
  Logback.
- Phase 3 (Foundation):
  - Maven Wrapper with checksum verification, reproducible build settings and toolchains
    (compiled on JDK 25 with `--release 21`, tests on JDK 21 and 25).
  - Quality gates: Spotless with palantir-java-format (ADR-009), Error Prone and NullAway in
    JSpecify mode, SpotBugs with Find Security Bugs, JaCoCo reports (coverage gate wired, enabled
    in Phase 4) and Javadoc lint.
  - GitHub Actions: CI matrix (JDK 21/25 × Logback 1.5.19, latest 1.5.x, latest 1.6.x),
    dependency review, CodeQL, OpenSSF Scorecard; Dependabot for Maven and GitHub Actions.
  - A Jazzer `@FuzzTest` spike on JUnit 6 (`docs/phase-3-findings.md`).
  - `LICENSE`, `NOTICE`, `CHANGELOG.md`, `CONTRIBUTING.md`, `.editorconfig`.
