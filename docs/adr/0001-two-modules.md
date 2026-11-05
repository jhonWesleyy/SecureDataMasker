# ADR-001: Two published modules, `core` and `logback`

- **Status:** Accepted (D3, approved 2026-11-04)
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

The library has two audiences (spec section 2). Some users only need to validate and normalize
Brazilian documents, with no logging involved. Others need the Logback integration. The library
also sees every log message an application writes, so its supply-chain surface must stay as small
as possible (spec section 1.4, threat T11).

## Decision

Publish two Maven artifacts that always share one version:

| Artifact | JPMS module | Depends on |
| --- | --- | --- |
| `securedatamasker-core` | `io.github.jhonwesleyy.securedatamasker.core` | nothing at runtime |
| `securedatamasker-logback` | `io.github.jhonwesleyy.securedatamasker.logback` | `core`; `logback-classic` in `provided` scope (ADR-007) |

Inside `core`, dependencies point one way only: `masking → detection → validation → model`. The
value types are in the base package `...core`. Implementation details live in `...core.internal`,
which `module-info.java` does not export (RNF09).

JSpecify (D9) is declared with Maven scope `provided` and as `requires static org.jspecify` in both
modules. Its annotations document the nullness contract and are not needed at runtime, so `core`
still has zero runtime dependencies (RNF04). A test (`ModuleDescriptorTest`) reads the compiled
descriptor and checks that only `java.base` is required non-statically and that only the four API
packages are exported.

### Deviation from the spec sketch

The spec places the value types "in `...core`" and lists `...core.internal` as not exported. Both
points are unchanged. One placement is now explicit: the line-masking layout that
`MaskingPatternLayoutEncoder` will use is **not** in the public API. It will be an internal class
of the `logback` module, created by the encoder itself, so there is no public layout type to keep
compatible after 1.0 (D11).

## Consequences

- The compiler enforces the boundary: `core` cannot import Logback.
- A future `log4j2` module can reuse `core` without touching it (spec section 6, item 4).
- Two artifacts must be released together; the parent POM keeps them on one version.
- Consumers on the module path must `requires io.github.jhonwesleyy.securedatamasker.logback`. The
  exported package needs no `opens`, because Logback instantiates the public classes through their
  public constructors.

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| A single module with Logback as `optional` | Users who only validate would see Logback types; coupling is easy to introduce without noticing; needs `requires static` for Logback in the only module |
| Three modules (`validation`, `masking`, `logback`) plus a BOM | `masking` always needs `validation` (the `VALIDATED` mode), so the split saves nothing and adds a BOM |
| JSpecify in `compile` scope | Would show up as a runtime dependency in `dependency:tree` and break the RNF04 check, for no benefit |
