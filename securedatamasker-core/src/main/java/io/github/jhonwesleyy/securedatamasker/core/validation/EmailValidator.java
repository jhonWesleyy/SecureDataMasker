package io.github.jhonwesleyy.securedatamasker.core.validation;

import org.jspecify.annotations.Nullable;

/**
 * Validates ASCII e-mail addresses (RF03) against the documented pragmatic subset based on the WHATWG HTML definition of a valid e-mail address, with at most 254 characters, a local part of at most 64 and a domain of at most 253.
 *
 * <p>Stateless and thread-safe. Never throws on invalid input and never includes the input in the
 * result (RF08).
 */
public final class EmailValidator {

    private EmailValidator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates and normalizes a value.
     *
     * @param input the value to validate; {@code null} yields {@link ValidationFailure#EMPTY}
     * @return {@link ValidationResult.Valid} with the address with the domain in lower case, or {@link ValidationResult.Invalid} with the
     *     typed reason
     */
    public static ValidationResult validate(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
