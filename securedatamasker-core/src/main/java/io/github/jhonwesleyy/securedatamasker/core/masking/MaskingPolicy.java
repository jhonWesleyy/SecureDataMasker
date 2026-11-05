package io.github.jhonwesleyy.securedatamasker.core.masking;

import org.jspecify.annotations.Nullable;

/**
 * How a value of one type is masked (RF12): mask character, whether display formatting is kept,
 * how many characters are revealed at the start and at the end, and the fixed length used for
 * variable-length segments.
 *
 * <p>Instances are immutable and thread-safe; every {@code with...} method returns a new
 * instance. The per-type meaning of each setting, and the policies behind the three {@link
 * MaskingPreset presets}, are specified in {@code docs/masking-policy.md}.
 *
 * {@snippet :
 * MaskingPolicy emailPolicy = MaskingPolicy.revealFirst(1).keepDomain();
 * }
 */
public final class MaskingPolicy {

    /** The default mask character. */
    public static final char DEFAULT_MASK_CHARACTER = '*';

    /**
     * The default number of mask characters that replace a variable-length segment (for example
     * the local part of an e-mail address), whatever the segment's real length.
     */
    public static final int DEFAULT_FIXED_MASK_LENGTH = 3;

    private MaskingPolicy() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a policy that reveals nothing, keeps display formatting and uses the default mask
     * character and fixed mask length.
     *
     * @return the fully masking policy
     */
    public static MaskingPolicy fullyMasked() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a policy that reveals the first {@code count} significant characters and masks the
     * rest.
     *
     * @param count the number of leading characters to reveal; must be non-negative
     * @return the new policy
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static MaskingPolicy revealFirst(int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a policy that reveals the last {@code count} significant characters and masks the
     * rest.
     *
     * @param count the number of trailing characters to reveal; must be non-negative
     * @return the new policy
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static MaskingPolicy revealLast(int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy that also reveals the first {@code count} significant
     * characters.
     *
     * @param count the number of leading characters to reveal; must be non-negative
     * @return the new policy
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public MaskingPolicy withRevealedPrefix(int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy that also reveals the last {@code count} significant
     * characters.
     *
     * @param count the number of trailing characters to reveal; must be non-negative
     * @return the new policy
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public MaskingPolicy withRevealedSuffix(int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy that keeps the domain of an e-mail address in clear. Ignored
     * for types other than e-mail.
     *
     * @return the new policy
     */
    public MaskingPolicy keepDomain() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy with another mask character.
     *
     * @param maskCharacter the mask character; must be a printable ASCII character that is not a
     *     letter, a digit, a line terminator or one of the type separators
     * @return the new policy
     * @throws IllegalArgumentException if the character is not allowed
     */
    public MaskingPolicy withMaskCharacter(char maskCharacter) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy that keeps or drops the type's display formatting in the
     * output.
     *
     * @param preserve {@code true} to keep separators such as {@code .}, {@code -} and {@code /}
     * @return the new policy
     */
    public MaskingPolicy withPreservedFormatting(boolean preserve) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a copy of this policy with another fixed mask length for variable-length segments.
     *
     * @param length the number of mask characters; must be at least 1
     * @return the new policy
     * @throws IllegalArgumentException if {@code length} is less than 1
     */
    public MaskingPolicy withFixedMaskLength(int length) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number of leading significant characters revealed.
     *
     * @return the revealed prefix length
     */
    public int revealedPrefixLength() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number of trailing significant characters revealed.
     *
     * @return the revealed suffix length
     */
    public int revealedSuffixLength() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns whether the domain of an e-mail address is kept in clear.
     *
     * @return {@code true} if the domain is kept
     */
    public boolean keepsDomain() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the mask character.
     *
     * @return the mask character
     */
    public char maskCharacter() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns whether display formatting is kept in the output.
     *
     * @return {@code true} if separators are kept
     */
    public boolean preservesFormatting() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the number of mask characters that replace a variable-length segment.
     *
     * @return the fixed mask length
     */
    public int fixedMaskLength() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Two policies are equal when all their settings are equal.
     *
     * @param other the object to compare with
     * @return {@code true} if {@code other} is a policy with the same settings
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

    /**
     * Returns a description of the settings. A policy holds no data value, so this is safe to log.
     *
     * @return a description of this policy
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
