# ADR-002: A hand-written linear scanner instead of regular expressions for detection

- **Status:** Accepted
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

Detection runs on every log event, and part of every event is controlled by external users
(threat T6). `java.util.regex` is a backtracking engine, and e-mail patterns are a classic ReDoS
case. One regex per type would also mean one pass per type, with overlaps that are hard to resolve
(spec section 11.3). RNF02 requires O(n) detection, and RNF05 requires a zero-allocation fast path
for messages with no candidates.

## Decision

1. **One pass, hand-written.** A candidate scanner in `core.internal` walks the text once. It
   collects runs of digits, letters and separators, and anchors e-mail candidates on `@`, with
   bounded expansion: at most 64 characters left for the local part and 253 right for the domain.
   Each candidate is checked by the type validators. No `java.util.regex` runs on this path.
   Validators may use bounded regular expressions for isolated values only after a length check,
   and never in the scanner.
2. **Fast path.** A text with no ASCII digit and no `@` is returned as it is after a single scan,
   with no allocation.
3. **Overlap resolution in one place** (RF14): leftmost first, then longest, then type priority in
   the declaration order of `SensitiveDataType`.
4. **Detection modes** (RF13, D4) per type, in `DetectionMode`:
   - `SHAPE`: mask anything with the type's shape.
   - `VALIDATED`: mask only what also passes full validation.
   - `SHAPE_OR_VALIDATED` (the default): formatted candidates are matched by shape; unformatted
     candidates must pass validation; phone numbers are detected only when formatted or prefixed
     with `+55`.
5. **No public detector extension point in 0.x** (see ADR-006).

### Deviation from the spec sketch: a third detection mode

The spec sketch has only `SHAPE` and `VALIDATED` (RF13). D4, however, defines a default that is
neither: "formatted → `SHAPE`; digits/alphanumeric only → `VALIDATED`; phone only with formatting or
`+55`". Expressing D4 with two constants would force every caller to know the input's shape in
advance, which is impossible for free text. A third constant states the approved default
directly.

It is named **`SHAPE_OR_VALIDATED`** rather than `SHAPE_IF_FORMATTED`, which was proposed during
planning. The short name only describes the first half of the rule and hides what happens to
unformatted input. `SHAPE_OR_VALIDATED` names both halves: shape when formatted, *or* validation
when not. Its Javadoc spells out both halves and the stricter phone rule with examples. This adds
no behavior beyond D4. The default itself is unchanged.

`TextMasker.detect(CharSequence)` returns `Match(start, end, type)`. A `Match` holds no text, so it
is safe to log.

## Consequences

- O(n) by construction. ReDoS is ruled out on the hot path, not just mitigated.
- More hand-written code to test. The counterweight is property-based tests and fuzzing (spec
  section 13). The tool choice is open: see `docs/phase-2-findings.md` (jqwik, Jazzer).
- Adding a type means changing the scanner, which is acceptable while the type set is closed
  (ADR-006).
- Non-ASCII digits are not detected (threat T8). This is documented as a limit.

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| One regex per type, applied in sequence | N passes; ReDoS risk; overlap resolution is spread across the patterns |
| A single regex with alternation | One pass, but unreadable and still backtracking |
| RE2/J or another linear-time regex engine | A runtime dependency in `core` (violates RNF04); it still cannot express check-digit validation |
| Aho-Corasick or a generated automaton | The patterns are structural, not literal strings |
| Two modes only, D4 encoded as "per-type default" | Cannot express "formatted vs. unformatted" for the same type |
