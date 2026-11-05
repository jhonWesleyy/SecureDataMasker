package io.github.jhonwesleyy.securedatamasker.core.validation;

import org.jspecify.annotations.Nullable;

/**
 * Validates Brazilian phone numbers (RF04): area code (DDD) from the official list, mobile numbers (9 digits starting with 9) and landlines (8 digits starting with 2 to 5), with or without the {@code +55} country code.
 *
 * <p>Stateless and thread-safe. Never throws on invalid input and never includes the input in the
 * result (RF08).
 */
public final class PhoneNumberValidator {

    private PhoneNumberValidator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates and normalizes a value.
     *
     * @param input the value to validate; {@code null} yields {@link ValidationFailure#EMPTY}
     * @return {@link ValidationResult.Valid} with the E.164 form, for example {@code +5511912345678}, or {@link ValidationResult.Invalid} with the
     *     typed reason
     */
    public static ValidationResult validate(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
