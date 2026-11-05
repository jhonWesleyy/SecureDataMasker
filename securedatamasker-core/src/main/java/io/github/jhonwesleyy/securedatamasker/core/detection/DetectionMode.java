package io.github.jhonwesleyy.securedatamasker.core.detection;

/**
 * How strictly a candidate must match before it is treated as sensitive data (RF13, D4, ADR-002).
 *
 * <p>The trade-off is between safety (mask more, including mistyped values) and observability (do
 * not destroy identifiers that merely look like sensitive data).
 */
public enum DetectionMode {
    /**
     * A candidate is masked when it has the type's shape, whatever its check digits.
     *
     * <p>Masks mistyped values too, at the cost of more false positives on digit-only identifiers.
     */
    SHAPE,

    /**
     * A candidate is masked only when it also passes the type's full validation (check digits,
     * area code, and so on).
     *
     * <p>Reduces false positives; a value with a typing error is left in clear.
     */
    VALIDATED,

    /**
     * The default (D4): the rule depends on whether the candidate carries the type's display
     * formatting.
     *
     * <ul>
     *   <li><strong>Formatted candidates are matched by shape</strong>, exactly as in {@link
     *       #SHAPE}. For example {@code 123.456.789-09} or {@code 12.ABC.345/01DE-35} is masked even
     *       if its check digits are wrong.
     *   <li><strong>Unformatted candidates must pass validation</strong>, exactly as in {@link
     *       #VALIDATED}. For example a bare run of 11 digits is masked as a CPF only if its check
     *       digits are correct.
     * </ul>
     *
     * <p>For {@link io.github.jhonwesleyy.securedatamasker.core.SensitiveDataType#PHONE} the
     * second half is stricter: a phone number is detected only when it is formatted or starts
     * with {@code +55}; a bare run of digits is never detected as a phone in this mode, because it
     * cannot be told apart from other numbers.
     */
    SHAPE_OR_VALIDATED
}
