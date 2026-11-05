package io.github.jhonwesleyy.securedatamasker.core.validation;

/**
 * Typed reasons why a value failed validation (RF08).
 *
 * <p>A reason describes the <em>kind</em> of failure only. It never carries the input, a fragment
 * of it, or its position.
 */
public enum ValidationFailure {
    /** The input is {@code null}, empty or contains only separators. */
    EMPTY,

    /** The input exceeds the maximum length accepted before any parsing is attempted. */
    INPUT_TOO_LONG,

    /** After removing separators, the number of significant characters is wrong for the type. */
    INVALID_LENGTH,

    /** The input contains a character that is not allowed for the type (for example a non-ASCII digit). */
    INVALID_CHARACTER,

    /** Separators are present but not in a position allowed by the type's display format. */
    INVALID_FORMAT,

    /** A check digit does not match the value computed from the other positions. */
    INVALID_CHECK_DIGIT,

    /** All digits are equal (for example a CPF made of a single repeated digit), which is rejected. */
    REPEATED_DIGITS,

    /** The phone number's area code (DDD) is not in the official list. */
    INVALID_AREA_CODE,

    /** The phone number's subscriber part is neither a valid mobile nor a valid landline number. */
    INVALID_SUBSCRIBER_NUMBER,

    /** The Pix key matches the format of more than one key type; the caller must state the type. */
    AMBIGUOUS_PIX_KEY_TYPE
}
