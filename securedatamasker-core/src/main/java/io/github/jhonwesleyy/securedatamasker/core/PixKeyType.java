package io.github.jhonwesleyy.securedatamasker.core;

/**
 * The five kinds of Pix key defined by the Central Bank's DICT rules.
 *
 * <p>The library validates only the <em>format</em> of a key. Whether a key is registered can only
 * be answered by the DICT, which is out of scope.
 */
public enum PixKeyType {
    /** A CPF, digits only (11 characters). */
    CPF,

    /** A CNPJ, numeric or alphanumeric, without punctuation (14 characters). */
    CNPJ,

    /** A lowercase e-mail address of at most 77 characters. */
    EMAIL,

    /** A Brazilian phone number in E.164 form, starting with {@code +55}. */
    PHONE,

    /** A random key (EVP): a UUID in its canonical lowercase 8-4-4-4-12 hexadecimal form. */
    EVP
}
