/**
 * Logback integration for SecureDataMasker: the {@code %mask(...)} composite converter and the
 * {@code MaskingPatternLayoutEncoder}.
 *
 * <p>Logback is not bundled: the application supplies {@code logback-classic} (provided scope,
 * ADR-007). The exported package is instantiated by Logback's configurator from {@code
 * logback.xml}, so it only needs to be exported, not opened.
 */
module io.github.jhonwesleyy.securedatamasker.logback {
    requires io.github.jhonwesleyy.securedatamasker.core;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;
    requires static transitive org.jspecify;

    exports io.github.jhonwesleyy.securedatamasker.logback;
}
