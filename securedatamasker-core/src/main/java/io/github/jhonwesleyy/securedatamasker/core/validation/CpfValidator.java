package io.github.jhonwesleyy.securedatamasker.core.validation;

import org.jspecify.annotations.Nullable;

/**
 * Validates CPFs (RF01): 11 ASCII digits, two mod-11 check digits, and rejection of repeated-digit sequences. Accepts the formatted ({@code 000.000.000-00}) and digits-only forms.
 *
 * <p>Stateless and thread-safe. Never throws on invalid input and never includes the input in the
 * result (RF08).
 */
public final class CpfValidator {

    private CpfValidator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates and normalizes a value.
     *
     * @param input the value to validate; {@code null} yields {@link ValidationFailure#EMPTY}
     * @return {@link ValidationResult.Valid} with the 11 digits, or {@link ValidationResult.Invalid} with the
     *     typed reason
     */
    public static ValidationResult validate(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
