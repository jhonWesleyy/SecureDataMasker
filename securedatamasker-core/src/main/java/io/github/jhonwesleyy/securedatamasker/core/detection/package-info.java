/**
 * Finding occurrences of sensitive data in free text.
 *
 * <p>Detection is a single linear pass over the input by a hand-written candidate scanner; no
 * backtracking regular expression runs on this path (RNF02, ADR-002). The scanner itself lives in
 * the non-exported {@code internal} package; this package holds only the type that configures
 * detection.
 */
@NullMarked
package io.github.jhonwesleyy.securedatamasker.core.detection;

import org.jspecify.annotations.NullMarked;
