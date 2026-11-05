/**
 * Masking policies and their application to known values and to free text.
 *
 * <p>The per-type masking rules, the three presets and before/after examples are specified in
 * {@code docs/masking-policy.md}. Masks have a fixed length: the number of mask characters never
 * reveals the length of the original value.
 *
 * <p>Masking is fail-closed (RNF03, ADR-003): if masking cannot be completed, the output is a
 * marker, never the original text.
 */
@NullMarked
package io.github.jhonwesleyy.securedatamasker.core.masking;

import org.jspecify.annotations.NullMarked;
