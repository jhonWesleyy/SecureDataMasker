package io.github.jhonwesleyy.securedatamasker.core;

import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * A valid CPF (Brazilian individual taxpayer number), safe to log by default.
 *
 * <p>Instances are immutable and created only through {@link #parse(CharSequence)} or {@link
 * #tryParse(CharSequence)}, which accept the formatted ({@code 000.000.000-00}) and the digits-only
 * forms. {@link #toString()} always returns a masked value; the value in clear is available only
 * through {@link #unmaskedValue()} and {@link #formatted()} (RF09, ADR-004).
 *
 * <p>This is a final class rather than a record on purpose: a record would generate a {@code
 * toString()} and a component accessor that expose the value in clear.
 */
public final class Cpf {

    private Cpf() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Parses and validates a CPF.
     *
     * @param input the CPF, formatted or digits only
     * @return the validated CPF
     * @throws InvalidValueException if the input is not a valid CPF; the exception carries only the
     *     typed reason, never the input
     */
    public static Cpf parse(CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Parses and validates a CPF without throwing on invalid input.
     *
     * @param input the CPF, formatted or digits only; {@code null} yields an empty result
     * @return the validated CPF, or empty if the input is {@code null} or not a valid CPF
     */
    public static Optional<Cpf> tryParse(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the CPF in clear, normalized to 11 ASCII digits.
     *
     * <p>Calling this method is an explicit decision to handle the value in clear.
     *
     * @return the 11 digits of the CPF
     */
    public String unmaskedValue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the CPF in clear, in the display format {@code 000.000.000-00} (RF07).
     *
     * @return the formatted CPF, in clear
     */
    public String formatted() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the CPF masked with the {@code BALANCED} policy, for example {@code ***.***.***-**}.
     *
     * @return the masked CPF; never the value in clear
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Two CPFs are equal when their normalized digits are equal.
     *
     * @param other the object to compare with
     * @return {@code true} if {@code other} is a CPF with the same digits
     */
    @Override
    public boolean equals(@Nullable Object other) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
