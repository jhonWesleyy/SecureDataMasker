# Contributing to SecureDataMasker

Thank you for your interest. The project is in its foundation phase: the public API is a skeleton
and every method throws `UnsupportedOperationException`. Read [`docs/spec.md`](docs/spec.md)
(Portuguese) and the [ADRs](docs/adr/) before proposing a change.

## Prerequisites

- **JDK 25 and JDK 21.** The code is compiled once, by JDK 25, with `--release 21` (D1,
  [ADR-008](docs/adr/0008-java-21-baseline.md)). The tests run on JDK 21 or JDK 25. Nothing is
  compiled by JDK 21.
- **`~/.m2/toolchains.xml`** declaring both JDKs. Maven selects them through
  `maven-toolchains-plugin` (compiler and Javadoc: JDK 25) and Surefire's `jdkToolchain` (tests:
  the JDK chosen with `-Dtest.jdk`). A version may be written as `21` or as a full version such
  as `21.0.9+10`. Example:

  ```xml
  <toolchains>
    <toolchain>
      <type>jdk</type>
      <provides><version>21</version></provides>
      <configuration><jdkHome>/usr/lib/jvm/java-21-openjdk-amd64</jdkHome></configuration>
    </toolchain>
    <toolchain>
      <type>jdk</type>
      <provides><version>25</version></provides>
      <configuration><jdkHome>/usr/lib/jvm/java-25-openjdk-amd64</jdkHome></configuration>
    </toolchain>
  </toolchains>
  ```

  `actions/setup-java` writes this file automatically in CI.
- **Run Maven on JDK 21 or later** (JDK 25 recommended). Spotless and the Maven JVM need it; the
  compiled code does not depend on it, because compilation always uses the JDK 25 toolchain.
- No local Maven installation is needed: always use the wrapper, `./mvnw` (`mvnw.cmd` on Windows).
  It downloads Maven 3.9.16 and checks it against `distributionSha256Sum`, and checks the wrapper
  jar against `wrapperSha256Sum` (`.mvn/wrapper/maven-wrapper.properties`). The wrapper jar is
  never committed.

`MAVEN_OPTS` is passed only to the Maven JVM, never to the forked test JVMs. If yours contains an
option that JDK 21 does not know (for example `--sun-misc-unsafe-memory-access=allow`), it does no
harm while Maven runs on JDK 25; if you run Maven itself on JDK 21, clear it (`MAVEN_OPTS=`).

## Build commands

```bash
./mvnw -B clean verify
```

```bash
./mvnw -B clean verify -Dtest.jdk=21
```

```bash
./mvnw -B clean verify -Dlogback.version=1.6.6
```

```bash
./mvnw spotless:apply
```

```bash
./mvnw -B dependency:tree -pl securedatamasker-core -Dscope=runtime
```

- The first command is the full build with every gate, tests on JDK 25, against the minimum
  supported Logback (1.5.19).
- `-Dtest.jdk=21` runs the same build with the tests on JDK 21.
- `-Dlogback.version=...` compiles and tests the `logback` module against another Logback version.
- `spotless:apply` formats the code; run it before committing.
- The last command must show `securedatamasker-core` alone: the core has no runtime dependencies
  (RNF04).

## Quality gates

`./mvnw verify` runs all of them, and CI runs the same build on every push and pull request. A
change is mergeable only when all of them pass.

| Gate | Tool | Fails the build when |
| --- | --- | --- |
| Formatting | Spotless + palantir-java-format ([ADR-009](docs/adr/0009-palantir-java-format.md)) | A Java file is not formatted |
| Compile-time bugs | Error Prone (`-Xlint:all`, `failOnWarning`) | Any javac or Error Prone warning |
| Nullness | NullAway in JSpecify mode, packages `io.github.jhonwesleyy.securedatamasker` | A nullness error |
| Tests | JUnit 6 on JDK 21 and 25 | A test fails |
| Bytecode and security | SpotBugs + Find Security Bugs (effort Max, threshold Low) | Any finding not excluded in `config/spotbugs-exclude.xml` |
| API documentation | `maven-javadoc-plugin`, `doclint=all`, `failOnWarnings` | Any Javadoc warning in the main sources |
| Module descriptors | `jar --describe-module` (CI) and `ModuleDescriptorTest` | An exported package, `requires` or `opens` changes unexpectedly |
| Runtime dependencies | `dependency:tree` (CI) | `securedatamasker-core` gains a runtime dependency |
| Coverage | JaCoCo | Reports only for now; the spec 13.2 thresholds are wired and **enabled in Phase 4** (`jacoco.check.skip`) |

Rules for exceptions:

- A SpotBugs exclusion goes in `config/spotbugs-exclude.xml`, as narrow as possible, with a comment
  that justifies it.
- An Error Prone suppression is local (`@SuppressWarnings` on the smallest element) and has a
  comment that justifies it. The current ones are listed in
  [`docs/phase-3-findings.md`](docs/phase-3-findings.md).

Mutation testing (PIT) arrives in Phase 4 and binary-compatibility checks (japicmp) from 1.0.

## Fuzzing

`securedatamasker-core` has a Jazzer `@FuzzTest` (`LongestDigitRunFuzzTest`). The normal build runs
it in regression mode, replaying the stored inputs under `src/test/resources/`. To fuzz locally:

```bash
JAZZER_FUZZ=1 JAZZER_MAX_DURATION=60s ./mvnw test -pl securedatamasker-core -Dtest=LongestDigitRunFuzzTest
```

Generated corpora (`.cifuzz-corpus/`) are not committed. Add an input to the test resources only
when it reproduces a real failure.

## Test data: fictitious only

Tests, examples and documentation use **fictitious data only** (CLAUDE.md rule 10, spec 13.3):

- CPFs and CNPJs generated by the check-digit algorithm in the test itself, or the public example
  of the Receita Federal;
- e-mail addresses under `example.com` or `example.org` (RFC 2606);
- phone numbers built from patterns.

Never use a value that belongs to a real person or company. Library code never puts an input value
in an exception, a Logback status message or `toString()` (rule 11).

## Logback versions

The library supports Logback **1.5.19 and later, including 1.6.x** (D14,
[ADR-007](docs/adr/0007-logback-provided-scope.md)). CI tests three versions:

| Leg | Where the version lives | Who updates it |
| --- | --- | --- |
| Minimum, 1.5.19 | `logback.version` in the parent `pom.xml` | Only the maintainer, by a recorded decision. Dependabot ignores `logback-classic` in the root build |
| Latest 1.5.x | `logback.version` in `.github/logback-matrix/1.5/pom.xml` | Dependabot, within 1.5.x (`>= 1.6` ignored) |
| Latest 1.6.x | `logback.version` in `.github/logback-matrix/1.6/pom.xml` | Dependabot, within 1.6.x (`>= 1.7` ignored) |

The two files under `.github/logback-matrix/` are not build modules; they only record versions.
The first job of `ci.yml` reads all three values, checks them, and passes them to the matrix.

They are separate files because Dependabot ignore rules match a dependency name, not a POM property.
Ignoring `logback-classic` in the root build protects the minimum; the separate files let the
"latest" legs be updated without touching it. A Dependabot pull request that changes either file
is reviewed like any other: CI runs the full matrix on it. A new release line (for example 1.7)
is not picked up automatically; adding it is a maintainer decision.

To test a version by hand, pass it with `-Dlogback.version=...` (see above).

## Commits and pull requests

- One phase per branch, delivered by pull request (CLAUDE.md, "Convenções de trabalho").
- Commit messages in the imperative mood, in one language consistently within a pull request.
- Formatting-only changes go in their own commit.
- Code, Javadoc and documentation are written in English (D13). The README exists in English and
  Brazilian Portuguese with identical code blocks.
