package io.github.jhonwesleyy.securedatamasker.core.validation;

/**
 * The outcome of validating a single value: either {@link Valid} with the normalized value, or
 * {@link Invalid} with a typed reason.
 *
 * <p>The interface is sealed so that callers can switch over it exhaustively:
 *
 * {@snippet :
 * switch (CpfValidator.validate(input)) {
 *     case ValidationResult.Valid valid -> store(valid.normalized());
 *     case ValidationResult.Invalid invalid -> reject(invalid.reason());
 * }
 * }
 */
public sealed interface ValidationResult {

    /**
     * Returns whether the value is valid.
     *
     * @return {@code true} for {@link Valid}, {@code false} for {@link Invalid}
     */
    boolean isValid();

    /**
     * A successful validation.
     *
     * <p>{@link #toString()} is overridden because the generated record implementation would print
     * the normalized value in clear (ADR-004).
     *
     * @param normalized the normalized value, in clear (RF06)
     */
    record Valid(String normalized) implements ValidationResult {

        /**
         * Returns {@code true}.
         *
         * @return {@code true}
         */
        @Override
        public boolean isValid() {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        /**
         * Returns a constant description that never contains the normalized value.
         *
         * @return a description without the value
         */
        @Override
        public String toString() {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    /**
     * A failed validation. It carries only the typed reason, never the input.
     *
     * @param reason why the value was rejected
     */
    record Invalid(ValidationFailure reason) implements ValidationResult {

        /**
         * Returns {@code false}.
         *
         * @return {@code false}
         */
        @Override
        public boolean isValid() {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
