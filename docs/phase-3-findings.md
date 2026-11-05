# Phase 3 findings

Phase 3 (Foundation) added the build, the quality gates, CI and the project conventions around the
Phase 2 API skeleton. It added no validation, detection or masking logic. This document records
what was verified, what deviates from a literal reading of the plan, and what is left open. Every
claim below was checked by a build, locally (Ubuntu OpenJDK 21.0.12 and 25.0.4) or in CI (Temurin
21.0.13 and 25.0.5).

## 1. Pinned versions

Checked against Maven Central and the actions' git tags on 2026-11-05; each is the latest stable
release on that date.

| Tool | Version | Notes |
| --- | --- | --- |
| Maven (via wrapper) | 3.9.16 | `distributionSha256Sum` `5af3b743…d89ce`; matches the `.sha512` on Maven Central and on downloads.apache.org |
| Maven Wrapper | 3.3.4, `distributionType=script` | `wrapperSha256Sum` `4e2fbf65…e4e1`; matches the `.sha1` on Maven Central. The jar is downloaded and checked by `./mvnw`, never committed |
| maven-toolchains-plugin | 3.3.0 | |
| maven-compiler-plugin | 3.16.0 | unchanged from Phase 2 |
| maven-surefire-plugin | 3.6.1 | unchanged from Phase 2 |
| maven-clean / resources / jar / install / deploy / site / dependency / help | 3.5.0 / 3.5.0 / 3.5.1 / 3.2.0 / 3.2.0 / 3.22.0 / 3.11.0 / 3.5.2 | pinned for reproducibility |
| Spotless (maven plugin) | 3.11.0 | |
| palantir-java-format | 2.103.0 | ADR-009 |
| Error Prone | 2.52.0 | |
| NullAway | 0.14.3 | |
| SpotBugs / spotbugs-maven-plugin | 4.10.5 / 4.10.5.0 | |
| Find Security Bugs | 1.14.0 | |
| JaCoCo | 0.8.16 | |
| maven-javadoc-plugin | 3.12.0 | |
| Jazzer (`jazzer-junit`) | 0.30.0 | test scope |
| JUnit | 6.1.3 | unchanged |

| GitHub Action | Version | Commit SHA |
| --- | --- | --- |
| actions/checkout | v7.0.1 | `3d3c42e5aac5ba805825da76410c181273ba90b1` |
| actions/setup-java | v6.2.0 | `77dd5b2ce225ea11469928ac317850245a74ae78` |
| actions/upload-artifact | v7.0.2 | `cf430e030ddbb5b0abf93d22962f4752f3646cd9` |
| actions/dependency-review-action | v5.0.0 | `a1d282b36b6f3519aa1f3fc636f609c47dddb294` |
| github/codeql-action (init, analyze, upload-sarif) | v4.39.3 | `33d7b85da1a04171e211befe2c739303b9a4c066` (commit of the annotated tag) |
| ossf/scorecard-action | v2.5.0 | `fa7117db83c52d787ae5a89f350999701505d78e` (commit of the annotated tag) |

## 2. Toolchains: compile once on JDK 25, test on 21 and 25 (D1)

- `maven-toolchains-plugin:toolchain` selects a JDK in `[25,26)` for the whole build. The compiler,
  Javadoc and SpotBugs plugins report `Toolchain in ...: JDK[.../25...]`.
- The compiler forks the toolchain's `javac` with `--release 21` (`Compiling ... with javac [forked
  debug release 21 module-path]`).
- Surefire's `jdkToolchain` uses `[${test.jdk},${test.jdk}.99999)`. The range matches a toolchain
  declared as `21` as well as one declared with a full version such as `21.0.9+10`; both forms
  were tested.
- CI checks the outcome rather than the configuration: after the tests, a step reads
  `java.specification.version` from every Surefire report and fails unless it equals the matrix
  JDK. In the first CI run, the JDK 21 legs report `21`, compiled by Temurin 25.0.5 and tested on
  Temurin 21.0.13.
- **Nothing is compiled on JDK 21**, so `-XDaddTypeAnnotationsToSymbol=true` (findings 2) is not
  needed. Even with Maven itself running on JDK 21 (`JAVA_HOME=…21 MAVEN_OPTS= ./mvnw verify
  -Dtest.jdk=21`), compilation still uses the JDK 25 toolchain and the build passes.
- `MAVEN_OPTS` (findings 1.5): Maven's own JVM is the only one that reads it; the forked test JVMs
  do not. CI clears it at the workflow level. Locally, the environment's
  `--sun-misc-unsafe-memory-access=allow` stays harmless as long as Maven runs on JDK 25.

## 3. Error Prone and NullAway

- **`--release 21` on JDK 25 works** with Error Prone 2.52.0 and NullAway 0.14.3. This closes the
  open point from Phase 2 findings, section 2 ("must verify with a real build").
- Because `javac` is forked (toolchain), `.mvn/jvm.config` does not reach it. The `jdk.compiler`
  `--add-exports` / `--add-opens` are therefore passed as `-J` compiler arguments. `.mvn/jvm.config`
  carries the same list, as findings section 2 asked, for tools that run inside the Maven JVM. With
  the current configuration nothing needs it: Spotless/palantir passed before the file existed. It
  is a safeguard, not a requirement.
- Proven to fail the build, with throw-away probe files that were not committed:
  - `return null;` from a non-null method fails with `[NullAway] returning @Nullable expression
    from method with @NonNull return type`.
  - Returning `List<@Nullable String>` as `List<String>` fails with `[NullAway] incompatible types`.
    Only JSpecify mode reports this, which proves that mode is active.
  - An Error Prone warning (reference equality on strings) fails through `failOnWarning`
    (`warnings found and -Werror specified`).
- What the skeleton triggered, and how it was handled:

| Finding | Where | Handling | Why |
| --- | --- | --- | --- |
| `[exports]` lint: "class Nullable in module org.jspecify is not indirectly exported using 'requires transitive'" | every `@Nullable` in the exported API (18 places) | **Resolved at the gate:** `requires static transitive org.jspecify` in both modules, and the `exports` lint category is enabled again (`-Xlint:all`) | Approved by the maintainer (section 8). Re-enabling the lint also flagged the `logback` module's exported API, which extends Logback types; that module now declares `requires transitive` on `ch.qos.logback.classic` and `ch.qos.logback.core` (ADR-007) |
| `DoNotCallSuggester` | every stub method | Check disabled (`-Xep:DoNotCallSuggester:OFF`), re-enable in Phase 4 | The check flags methods whose body only throws, which is every stub |
| `UnusedVariable` | `InvalidValueException.reason` | `@SuppressWarnings("UnusedVariable")` on the field, with a comment | The stub `reason()` throws, so the field is never read. Remove in Phase 4 |
| `EscapedEntity` | `Cnpj.toString()` Javadoc | **Fixed**: `{@code …&#47;…}` replaced by `<code>…&#47;…</code>` | A real Javadoc bug: the entity would have been rendered literally |

## 4. SpotBugs with Find Security Bugs

Effort `Max`, threshold `Low`, on the main code of both modules. The skeleton produced 11 findings.
All of them come from stubs that only throw; none points at a real defect. Each one is excluded in
`config/spotbugs-exclude.xml`, narrowed to the class and member, with a comment and a removal
phase:

| Pattern | Classes | Cause | Remove in |
| --- | --- | --- | --- |
| `EQ_UNUSUAL` (6) | `Cpf`, `Cnpj`, `Email`, `PhoneNumber`, `PixKey`, `MaskingPolicy` | `equals` only throws | Phase 4 |
| `UWF_UNWRITTEN_FIELD` (3) | `internal.Match` | The compact constructor only throws, so the record fields are never written | Phase 4 |
| `CT_CONSTRUCTOR_THROW` (2) | `MaskingConverter`, `MaskingPatternLayoutEncoder` | Non-final constructors only throw | Phase 5 |

## 5. JaCoCo and Javadoc

- JaCoCo writes its reports (`target/site/jacoco/`) on every build. The `check` goal carries the
  section 13.2 thresholds (core: lines 0.95, branches 0.90; logback: lines 0.85) but is skipped by
  `jacoco.check.skip=true`. With `-Djacoco.check.skip=false`, it fails on the stubs as expected
  ("Coverage checks have not been met"). It is enabled in Phase 4.
- **Javadoc gotcha:** with `<quiet>true</quiet>`, maven-javadoc-plugin 3.12.0 hides the warnings
  from its own `failOnWarnings` check. A public method with no comment passed the build. With
  `quiet=false`, the same probe fails ("Project contains Javadoc Warnings"). The POM documents
  this.
- `doclint=all` passes on the current API. A missing comment fails the build. Broken `{@link}`
  targets are caught even earlier, by Error Prone during compilation.

## 6. Jazzer and JUnit 6 (D16)

**Result: it works. Jazzer is kept.**

- `jazzer-junit` 0.30.0 declares JUnit 5.9.0 / Platform 1.9.0. The JUnit 6.1.3 BOM aligns them:
  `dependency:tree` shows `junit-platform-commons` and `junit-platform-launcher` at 6.1.3 under
  `jazzer-junit`. JUnit was not downgraded.
- `LongestDigitRunFuzzTest` (core test sources) has one `@FuzzTest` on a self-contained target
  defined in the test itself. In the normal build it runs in **regression mode**: four invocations,
  one per stored input (three seeds under `src/test/resources/.../LongestDigitRunFuzzTestInputs/`)
  plus the empty input. It passes on JDK 21 and 25, locally and in all six CI legs.
- Negative check: weakening the property so that a stored seed violates it fails the build
  (`Tests run: 4, Errors: 1`). Regression mode really executes the inputs.
- Fuzzing mode also works locally: `JAZZER_FUZZ=1 JAZZER_MAX_DURATION=15s` ran 5,201,298 inputs
  in 16 seconds on JDK 25 with no failure.
- Side effects to keep in mind for Phase 4 and Phase 6:
  - Jazzer installs a Java agent dynamically in the test JVM. JDK 21 prints "A Java agent has been
    loaded dynamically … will be disallowed by default in a future release". A later JDK may need
    `-XX:+EnableDynamicAgentLoading` in Surefire's `argLine`.
  - The agent instruments every test class of the module, including `ModuleDescriptorTest`, with
    Jazzer's sanitizer hooks. It is harmless today; keep it in mind if a test ever measures timing.
  - `jazzer` ships native code (libFuzzer). It stays in test scope, so it is not part of any
    published artifact.
  - Local fuzzing writes `.cifuzz-corpus/`, which is git-ignored.
- Not decided here: the long fuzzing job (spec 14.2, "nightly") and the real scanner fuzz targets
  belong to Phase 4 and later.

## 7. CI, Dependabot and the Logback matrix

- First CI run on the branch: all six legs passed (JDK 21/25 × Logback 1.5.19, 1.5.38, 1.6.6);
  CodeQL passed. Dependency review runs only on pull requests.
- The PoC test `reportsTheLogbackVersionUnderTest` asserts that the `logback-classic` jar on the
  test path is the one the leg asked for, so a green leg proves it tested that version (the
  1.5.19 log shows `logback-classic-1.5.19.jar`).
- **Reproducible build:** two consecutive `clean package` runs produced byte-identical jars and
  Javadoc jars (same SHA-256). This is not yet the release-time check of spec 14.2 (two
  independent builds compared in CI), which is Phase 8.
- **Logback "latest" mechanism.** Dependabot ignore rules match a dependency name, not a POM
  property. If the minimum and the latest versions shared the parent POM, any rule that let
  Dependabot update one would also let it update the other. The latest 1.5.x and 1.6.x versions
  therefore live in two non-module POMs, `.github/logback-matrix/1.5/pom.xml` and
  `.github/logback-matrix/1.6/pom.xml`. Each has its own Dependabot entry, limited to its release
  line (`>= 1.6` and `>= 1.7` ignored). The root entry ignores `logback-classic` completely, so
  the 1.5.19 minimum (ADR-007) is never proposed for update. The first CI job reads the three
  values with `sed`, checks them (format, line, and that the minimum is still 1.5.19), and feeds
  the matrix.
- **What could be verified about Dependabot, and what could not.** `dependabot.yml` validates
  against the SchemaStore `dependabot-2.0` schema, and the workflows against the
  `github-workflow` schema and `actionlint` 1.7.12. Dependabot itself cannot be run from a branch:
  its behavior (which PRs it opens) becomes visible only after the file reaches the default branch.
  Two points to check after the merge, in *Insights → Dependency graph → Dependabot*:
  1. it opens no PR for `logback-classic` in `/`;
  2. whether it updates the versions given as properties inside plugin configuration
     (palantir-java-format) and in `annotationProcessorPaths` (Error Prone, NullAway). Dependabot's
     coverage of these locations is not documented clearly. If it skips them, they are updated by
     hand. CONTRIBUTING.md does not claim otherwise.

## 8. Maintainer decisions (gate review, 2026-11-05)

| # | Question | Decision | Recorded in |
| --- | --- | --- | --- |
| 1 | `requires static` vs `requires static transitive org.jspecify` (section 3) | **`requires static transitive`** in both modules; the `exports` lint is enabled again. Re-enabling it also required `requires transitive` on `ch.qos.logback.classic` and `ch.qos.logback.core` in the `logback` module, whose exported API extends Logback types | ADR-001, ADR-007, `module-info.java`, CI descriptor check |
| 2 | ADR-009 (formatter) | **Approved**: palantir-java-format | ADR-009 |
| 3 | Dependency review failed on license expressions of build-only Apache Maven plugins (`maven-help-plugin`, `maven-javadoc-plugin`, `maven-site-plugin`) | **False positive**: the plugins are Apache-2.0; the extra identifiers (`LicenseRef-scancode-*`, `SAX-PD`, `xpp`) come from scancode-detected third-party files bundled inside them, and nothing from them ships in our JARs. Exempted by name with `allow-dependencies-licenses`; every other dependency is still checked. A plugin flagged the same way in the future must be added explicitly | `.github/workflows/ci.yml` |

The first Dependency review run also failed because the repository's Dependency graph was off; the
maintainer enabled it in the repository settings.

## Sources

- Error Prone installation (Maven, `-J` flags for forked compilation):
  <https://errorprone.info/docs/installation>
- NullAway JSpecify support: <https://github.com/uber/NullAway/wiki/JSpecify-Support>
- Maven Toolchains: <https://maven.apache.org/guides/mini/guide-using-toolchains.html>
- Surefire `jdkToolchain`:
  <https://maven.apache.org/surefire/maven-surefire-plugin/examples/toolchains.html>
- Maven Wrapper (`distributionType`, checksums): <https://maven.apache.org/tools/wrapper/>
- Dependabot options (`ignore`, `groups`, multiple directories):
  <https://docs.github.com/en/code-security/dependabot/working-with-dependabot/dependabot-options-reference>
- Jazzer JUnit integration: <https://github.com/CodeIntelligenceTesting/jazzer>
