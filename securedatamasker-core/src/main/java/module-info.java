/**
 * Validation, normalization, detection and masking of Brazilian sensitive data.
 *
 * <p>This module has no runtime dependencies (RNF04). JSpecify is required statically: its
 * annotations document the nullness contract but are not needed at run time.
 *
 * <p>Only the public API packages are exported. The {@code internal} package holds the scanner and
 * lookup tables and is deliberately not exported (RNF09).
 */
module io.github.jhonwesleyy.securedatamasker.core {
    requires static transitive org.jspecify;

    exports io.github.jhonwesleyy.securedatamasker.core;
    exports io.github.jhonwesleyy.securedatamasker.core.validation;
    exports io.github.jhonwesleyy.securedatamasker.core.detection;
    exports io.github.jhonwesleyy.securedatamasker.core.masking;
}
