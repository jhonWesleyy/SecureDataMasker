package io.github.jhonwesleyy.securedatamasker.core.validation;

import io.github.jhonwesleyy.securedatamasker.core.PixKeyType;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Validates the format of Pix keys under the DICT rules (RF05).
 *
 * <p>This validator composes the other validators and adds only the DICT-specific rules: CPF and
 * CNPJ without punctuation, e-mail in lower case with at most 77 characters, phone in E.164 form
 * with {@code +55}, and EVP as a canonical UUID.
 *
 * <p>Stateless and thread-safe. Never throws on invalid input and never includes the input in the
 * result.
 */
public final class PixKeyValidator {

    private PixKeyValidator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates a key against the format of a known type.
     *
     * @param input the key; {@code null} yields {@link ValidationFailure#EMPTY}
     * @param type the expected key type
     * @return {@link ValidationResult.Valid} with the key in the normalized form required by the
     *     DICT, or {@link ValidationResult.Invalid} with the typed reason
     */
    public static ValidationResult validate(@Nullable CharSequence input, PixKeyType type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Validates a key of unknown type, inferring the type from its format.
     *
     * @param input the key; {@code null} yields {@link ValidationFailure#EMPTY}
     * @return {@link ValidationResult.Valid} if exactly one type matches; otherwise {@link
     *     ValidationResult.Invalid}, with {@link ValidationFailure#AMBIGUOUS_PIX_KEY_TYPE} when more
     *     than one type matches
     */
    public static ValidationResult validate(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns every key type whose format the input satisfies.
     *
     * <p>An empty set means the input is not a valid key of any type; a set with more than one
     * element signals an ambiguity (RF05) that the caller must resolve, typically by asking the
     * user which type was meant.
     *
     * @param input the key; {@code null} yields an empty set
     * @return an unmodifiable set of matching key types
     */
    public static Set<PixKeyType> inferTypes(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
