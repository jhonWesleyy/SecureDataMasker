/**
 * Deterministic validators and normalizers for single values.
 *
 * <p>Validators never throw on invalid input and never echo the input: they return a {@link
 * io.github.jhonwesleyy.securedatamasker.core.validation.ValidationResult} whose failure branch
 * carries only a typed {@link io.github.jhonwesleyy.securedatamasker.core.validation.ValidationFailure
 * reason} (RF08). Only ASCII digits {@code '0'..'9'} are accepted as digits, and case folding uses
 * {@link java.util.Locale#ROOT}.
 */
@NullMarked
package io.github.jhonwesleyy.securedatamasker.core.validation;

import org.jspecify.annotations.NullMarked;
