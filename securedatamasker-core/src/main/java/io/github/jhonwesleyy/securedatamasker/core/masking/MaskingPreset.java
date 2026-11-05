package io.github.jhonwesleyy.securedatamasker.core.masking;

import io.github.jhonwesleyy.securedatamasker.core.SensitiveDataType;

/**
 * Predefined sets of per-type masking policies (D5).
 *
 * <p>The exact output of each preset for each type is specified, with examples, in {@code
 * docs/masking-policy.md}. All presets use fixed-length masks.
 */
public enum MaskingPreset {
    /** Reveals nothing for any type. */
    STRICT,

    /**
     * The default (D5). CPF and CNPJ are fully masked; e-mail reveals the first character of the
     * local part and the domain; phone and EVP reveal their last four characters.
     */
    BALANCED,

    /**
     * Reveals more for debugging: CPF reveals its two check digits, CNPJ its first two and last
     * two characters, e-mail the first and last characters of the local part plus the domain, and
     * phone also the area code; EVP is masked as in {@link #BALANCED}. Partially revealed data may
     * still be personal data under the LGPD.
     */
    PARTIAL_REVEAL;

    /**
     * Returns the policy this preset applies to a type.
     *
     * @param type the data type
     * @return the policy for {@code type}
     */
    public MaskingPolicy policyFor(SensitiveDataType type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
