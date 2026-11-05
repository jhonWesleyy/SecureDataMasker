package io.github.jhonwesleyy.securedatamasker.core.validation;

import org.jspecify.annotations.Nullable;

/**
 * Validates CNPJs, numeric and alphanumeric, with a single algorithm (RF02, ADR-005): 12 positions in {@code [0-9A-Z]} plus 2 numeric check digits, each character valued as its ASCII code minus 48, mod 11 with weights 2 to 9. Lower-case letters are accepted and normalized to upper case.
 *
 * <p>Stateless and thread-safe. Never throws on invalid input and never includes the input in the
 * result (RF08).
 */
public final class CnpjValidator {

    private CnpjValidator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates and normalizes a value.
     *
     * @param input the value to validate; {@code null} yields {@link ValidationFailure#EMPTY}
     * @return {@link ValidationResult.Valid} with the 14 upper-case characters without punctuation, or {@link ValidationResult.Invalid} with the
     *     typed reason
     */
    public static ValidationResult validate(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
