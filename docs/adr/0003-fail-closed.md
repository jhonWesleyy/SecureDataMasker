# ADR-003: Fail-closed masking

- **Status:** Accepted
- **Date:** 2026-11-05
- **Phase:** 2 (Architecture)

## Context

If the masker throws or gives up, the easiest fallback is to write the original text. That is
exactly the leak the library exists to prevent (threat T10). Very large messages are a related
case: scanning them costs CPU on the logging path (threat T7), so the scan must be capped, and the
spec does not yet say what happens to the text above the cap.

## Decision

1. **Failure → marker.** If masking fails for any reason (an unexpected exception, an internal
   inconsistency), `TextMasker.mask` returns `TextMasker.MASKING_FAILED_MARKER`
   (`[MASKING_FAILED]`) in place of the whole text, and never throws. The Logback components write
   the marker and report a status message without any log content. Logging keeps working (spec
   section 16).
2. **Over-limit → marker for the whole text.** `TextMasker.Builder.maxScanLength(int)` sets the
   cap. The default is `DEFAULT_MAX_SCAN_LENGTH`, 1,048,576 characters, which is provisional until
   the first JMH round in Phase 4. A text longer than the cap is **not scanned**, and the output is
   `TextMasker.INPUT_TOO_LONG_MARKER` (`[MASKING_SKIPPED_INPUT_TOO_LONG]`) in place of the
   **whole** text. `MaskingPatternLayoutEncoder.setMaxScanLength` applies the same rule per
   rendered line: the line is replaced by the marker followed by the line separator.
3. **Unknown value → marker.** `TextMasker.maskValue(type, value)` returns
   `[MASKING_FAILED]` when the value is not recognized as `type`, never the value.
4. **`detect` is not fail-closed** because it returns positions, not text. It throws
   `IllegalArgumentException` on over-limit input. Its message never contains the input.

Both markers contain no digit and no `@`, so they are never detected again (idempotence), and no
line terminator.

### Why the whole text, not just the excess

The alternative was to scan the first `maxScanLength` characters, mask them, and replace only the
excess with a marker. It was rejected because a value crossing the boundary would leak in part.
For example, with the cap falling after `123.456.` in `... 123.456.789-09`, the prefix holds only
`123.456.`, which is not a complete candidate, so it would be emitted in clear. Avoiding that would
need look-ahead across the boundary, which means scanning past the cap anyway. Replacing the
whole text is simple and cannot leak. Losing a multi-megabyte line is the accepted cost, and the
cap is configurable.

## Consequences

- No code path writes unmasked text after a failure.
- An oversized message is lost from the log. Operators see the marker and can raise the limit. The
  default is deliberately far above the size of normal log lines, stack traces included.
- Tests must cover a detector that throws, a text one character over the limit, and the markers'
  idempotence (Phase 4).

## Alternatives considered

| Alternative | Why rejected |
| --- | --- |
| Fail-open (write the original text) | Defeats the purpose; T10 |
| Throw to the caller / to Logback | Logback would drop or report the event inconsistently; an exception message is one more leak path |
| Truncate: masked prefix plus a marker for the excess | Partial leak of a value crossing the cap (see above) |
| No cap | Unbounded CPU per event under adversarial input (T7) |
