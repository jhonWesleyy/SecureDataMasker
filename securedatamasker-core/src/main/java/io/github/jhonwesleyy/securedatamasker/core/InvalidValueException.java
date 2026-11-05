package io.github.jhonwesleyy.securedatamasker.core;

import io.github.jhonwesleyy.securedatamasker.core.validation.ValidationFailure;
import java.io.Serial;

/**
 * Thrown by the {@code parse} factories of the value types when the input is not valid.
 *
 * <p>The exception carries only the typed {@link ValidationFailure reason}. Its message is derived
 * from the reason alone and <strong>never</strong> contains the input value or any part of it
 * (CLAUDE.md rule 11, RNF01).
 *
 * <p>The spec sketch called this type {@code InvalidDocumentException}; it was renamed because
 * e-mail addresses, phone numbers and Pix keys are not documents (ADR-004).
 */
public final class InvalidValueException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 1L;

    /** The typed reason why the input was rejected. */
    // Phase 3: the skeleton's reason() throws, so the field is never read yet. Remove the
    // suppression when Phase 4 implements it (docs/phase-3-findings.md).
    @SuppressWarnings("UnusedVariable")
    private final ValidationFailure reason;

    /**
     * Creates an exception for the given reason. The message is derived from {@code reason} only.
     *
     * @param reason why the input was rejected
     */
    public InvalidValueException(ValidationFailure reason) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the typed reason why the input was rejected.
     *
     * @return the rejection reason, never {@code null}
     */
    public ValidationFailure reason() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
