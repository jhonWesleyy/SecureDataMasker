/**
 * Logback integration.
 *
 * <ul>
 *   <li>{@link io.github.jhonwesleyy.securedatamasker.logback.MaskingPatternLayoutEncoder} is the
 *       recommended integration (D8): it masks the complete line rendered by the pattern,
 *       including the throwable that {@code PatternLayout} appends implicitly when the pattern has
 *       no {@code %ex}.
 *   <li>{@link io.github.jhonwesleyy.securedatamasker.logback.MaskingConverter}, registered as
 *       {@code %mask(...)}, is the advanced option: it masks only the sub-pattern it wraps.
 * </ul>
 *
 * <p><strong>Limits.</strong> Both act only on text produced by a {@code PatternLayout}. JSON
 * encoders, appenders that read the {@code ILoggingEvent} directly (such as OpenTelemetry) and
 * output that does not go through Logback are not covered (spec section 8.1, threat T4).
 */
@NullMarked
package io.github.jhonwesleyy.securedatamasker.logback;

import org.jspecify.annotations.NullMarked;
