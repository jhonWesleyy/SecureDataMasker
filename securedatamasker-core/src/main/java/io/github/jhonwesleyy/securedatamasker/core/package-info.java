/**
 * Safe-by-default value types for Brazilian sensitive data.
 *
 * <p>{@link io.github.jhonwesleyy.securedatamasker.core.Cpf}, {@link
 * io.github.jhonwesleyy.securedatamasker.core.Cnpj}, {@link
 * io.github.jhonwesleyy.securedatamasker.core.Email}, {@link
 * io.github.jhonwesleyy.securedatamasker.core.PhoneNumber} and {@link
 * io.github.jhonwesleyy.securedatamasker.core.PixKey} can only be created through validating
 * factories, are immutable, and return a <em>masked</em> value from {@code toString()}. The value
 * in clear is available only through the explicit {@code unmaskedValue()} method (RF09, ADR-004).
 *
 * <p>No type in this package ever includes an input value in an exception message or in {@code
 * toString()}.
 */
@NullMarked
package io.github.jhonwesleyy.securedatamasker.core;

import org.jspecify.annotations.NullMarked;
