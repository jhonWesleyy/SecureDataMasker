package io.github.jhonwesleyy.securedatamasker.core.fuzz;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.code_intelligence.jazzer.junit.FuzzTest;

/**
 * Phase 3 spike (D16): checks that a real Jazzer {@code @FuzzTest} runs on JUnit 6.
 *
 * <p>No library logic exists yet (Phase 4), so the target is a tiny self-contained function defined
 * here, in the test sources: the length of the longest run of ASCII digits in a string. The
 * properties are the kind the Phase 4 scanner tests will need: it never throws, and its result is
 * bounded by the input length.
 *
 * <p>The normal build runs this test in <em>regression mode</em>: Jazzer only replays the inputs
 * stored in {@code src/test/resources/.../LongestDigitRunFuzzTestInputs/} plus an empty input. It
 * fuzzes only when {@code JAZZER_FUZZ=1} is set. The result of the spike is recorded in {@code
 * docs/phase-3-findings.md}.
 */
class LongestDigitRunFuzzTest {

    @FuzzTest
    void resultIsBoundedByTheInputLength(byte[] input) {
        String text = new String(input, java.nio.charset.StandardCharsets.ISO_8859_1);

        int run = longestDigitRun(text);

        assertTrue(run >= 0 && run <= text.length(), "run length out of bounds");
    }

    /** The fuzz target: one linear pass, no regex. */
    static int longestDigitRun(String text) {
        int longest = 0;
        int current = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9') {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 0;
            }
        }
        return longest;
    }
}
