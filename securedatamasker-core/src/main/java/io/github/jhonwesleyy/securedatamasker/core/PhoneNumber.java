package io.github.jhonwesleyy.securedatamasker.core;

import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * A valid Brazilian phone number, landline or mobile, safe to log by default.
 *
 * <p>Validation (RF04) checks the area code against the official list, mobile numbers (9 digits
 * starting with 9) and landlines (8 digits starting with 2 to 5), with or without the {@code +55}
 * country code. The value is normalized to E.164 ({@code +55} followed by the area code and the
 * subscriber number).
 *
 * <p>Instances are immutable and created only through the factories. {@link #toString()} always
 * returns a masked value; the value in clear is available only through {@link #unmaskedValue()},
 * {@link #e164()} and {@link #formatted()} (RF09, ADR-004).
 */
public final class PhoneNumber {

    private PhoneNumber() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Parses and validates a Brazilian phone number.
     *
     * @param input the phone number, formatted or not, with or without {@code +55}
     * @return the validated phone number
     * @throws InvalidValueException if the input is not a valid number; the exception carries only
     *     the typed reason, never the input
     */
    public static PhoneNumber parse(CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Parses and validates a Brazilian phone number without throwing on invalid input.
     *
     * @param input the phone number; {@code null} yields an empty result
     * @return the validated phone number, or empty if the input is {@code null} or not valid
     */
    public static Optional<PhoneNumber> tryParse(@Nullable CharSequence input) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number in clear, as area code and subscriber number digits without the country
     * code (10 or 11 digits).
     *
     * @return the national number, in clear
     */
    public String unmaskedValue() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number in clear, in E.164 form, for example {@code +5511912345678}.
     *
     * @return the E.164 number, in clear
     */
    public String e164() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number in clear, in the display format {@code (11) 91234-5678} or {@code (11)
     * 3123-4567} (RF07).
     *
     * @return the formatted number, in clear
     */
    public String formatted() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns whether this is a mobile number (9-digit subscriber number).
     *
     * @return {@code true} for a mobile number, {@code false} for a landline
     */
    public boolean isMobile() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number masked with the {@code BALANCED} policy, for example {@code (**)
     * *****-5678}. Landlines are masked to the same shape as mobile numbers.
     *
     * @return the masked number; never the value in clear
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Two phone numbers are equal when their E.164 forms are equal.
     *
     * @param other the object to compare with
     * @return {@code true} if {@code other} is a phone number with the same E.164 form
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
