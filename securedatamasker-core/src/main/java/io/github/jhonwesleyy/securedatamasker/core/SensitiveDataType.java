package io.github.jhonwesleyy.securedatamasker.core;

/**
 * The kinds of sensitive data that the library can detect and mask in free text.
 *
 * <p>Pix keys of type CPF, CNPJ, e-mail and phone are detected and masked as their underlying type.
 * Only the random Pix key (EVP, a UUID) has a type of its own, {@link #PIX_EVP}.
 *
 * <p>This set is closed in the 0.x series; see ADR-006 for why there is no extension point yet.
 */
public enum SensitiveDataType {
    /** Brazilian individual taxpayer number (11 digits, two mod-11 check digits). */
    CPF,

    /** Brazilian company taxpayer number, numeric or alphanumeric (ADR-005). */
    CNPJ,

    /** ASCII e-mail address, as defined in {@code docs/masking-policy.md} and RF03. */
    EMAIL,

    /** Brazilian landline or mobile phone number, with or without the {@code +55} country code. */
    PHONE,

    /** Random Pix key (EVP): a UUID in its canonical 8-4-4-4-12 hexadecimal form. */
    PIX_EVP
}
