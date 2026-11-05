# ADR-004: Value types return a masked `toString()`

- **Status:** Accepted
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

The most common leak is not a formatted message but an object logged whole: a DTO, an entity or an
exception that embeds a value (spec section 2, threat T5). Masking the rendered text treats the
symptom. A value type that is safe to log treats the cause (UC2, RF09).

## Decision

1. `Cpf`, `Cnpj`, `Email`, `PhoneNumber` and `PixKey` are **final classes**, not records. They are
   created only by validating factories (`parse`, which throws, and `tryParse`, which returns an
   `Optional`), and they are immutable.
2. `toString()` always returns the value masked with the `BALANCED` preset (`docs/masking-policy.md`).
3. The value in clear is available only through explicitly named methods: `unmaskedValue()`, and
   `formatted()` / `e164()` where RF07 applies. There is no getter named after a component.
4. Validation failures are reported by type only (RF08, CLAUDE.md rule 11):
   - `InvalidValueException` carries a `ValidationFailure` reason, and its message is derived from
     the reason alone.
   - `ValidationResult.Invalid` carries only the reason.
5. `ValidationResult.Valid` is a record (pattern-matching ergonomics), but **it overrides
   `toString()`** so that the normalized value is never printed.

### Deviations from the spec sketch (section 9.2)

| Sketch | Final | Why |
| --- | --- | --- |
| `InvalidDocumentException` | `InvalidValueException` (extends `IllegalArgumentException`) | E-mail addresses, phone numbers and Pix keys are not documents. Extending `IllegalArgumentException` matches how callers already treat bad input. |
| `record Valid(String normalized)` with its generated `toString()` | Same record, `toString()` overridden | The generated `toString()` would print the normalized value in clear, which violates RNF01 |
| `ValidationResult` without helpers | Adds `isValid()` | Lets callers avoid a `switch` for simple checks; adds no new state |
| Not specified | `PixKey.parse(input)` without a type fails with `AMBIGUOUS_PIX_KEY_TYPE` when more than one type matches; `PixKeyValidator.inferTypes` returns every matching type | RF05 requires the ambiguity to be signaled, not resolved by guessing |
| Not specified | `ValidationFailure` gains `EMPTY`, `INVALID_FORMAT`, `INVALID_AREA_CODE`, `INVALID_SUBSCRIBER_NUMBER` and `AMBIGUOUS_PIX_KEY_TYPE` | RF08 lists its reasons with "...". These cover the phone, Pix and null/empty cases. |

## Consequences

- Logging a value type, or an object whose `toString()` includes one, is safe by default.
- Only code that adopts the types is protected (T5 residual risk). This is documented.
- A record would have been shorter, but a record's generated `toString()` and component accessor
  expose the value. Overriding both is possible but easy to get wrong in one type.
- `equals` and `hashCode` use the normalized value, so two spellings of the same CPF are equal.
- Java cannot guarantee that a `String` is erased from memory. The types promise not to *print* the
  value, not to wipe it (spec section 8.2).

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| Records with overridden `toString()` | The generated accessor (`value()`) still reads as an innocent getter; the clear-text access must be explicit |
| `toString()` in clear plus a `masked()` method | Wrong default: the leak happens precisely when nobody thinks about it |
| Exceptions carrying the input "for debugging" | Violates CLAUDE.md rule 11 and RNF01 |
