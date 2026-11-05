package io.github.jhonwesleyy.securedatamasker.core.internal;

import io.github.jhonwesleyy.securedatamasker.core.SensitiveDataType;

/**
 * The location of one detected occurrence of sensitive data in a text.
 *
 * <p>A match records only where the occurrence is and what type it is, never the matched text, so
 * a match can be logged or stored without exposing the value.
 *
 * <p>Internal: produced by the scanner and consumed by the masking step. It is not part of the
 * public API (ADR-002).
 *
 * @param start the index of the first character of the occurrence, inclusive
 * @param end the index after the last character of the occurrence, exclusive
 * @param type the type of the occurrence
 */
public record Match(int start, int end, SensitiveDataType type) {

    /**
     * Creates a match.
     *
     * @param start the index of the first character, inclusive; must be non-negative
     * @param end the index after the last character, exclusive; must be greater than {@code start}
     * @param type the type of the occurrence
     * @throws IllegalArgumentException if the indices are out of order or negative
     */
    public Match {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the length of the occurrence.
     *
     * @return {@code end - start}
     */
    public int length() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
