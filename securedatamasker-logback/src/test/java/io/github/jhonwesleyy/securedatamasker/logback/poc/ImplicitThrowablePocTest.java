package io.github.jhonwesleyy.securedatamasker.logback.poc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.JsonEncoder;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Phase 2 proof of concept: behavior of <em>plain</em> Logback, with no SecureDataMasker code.
 *
 * <p>Documents the implicit {@code %ex} bypass (spec section 8.2, threat T2) and why {@code
 * %replace(%msg)} is not enough. Results are recorded in {@code docs/phase-2-findings.md}.
 *
 * <p>Run against a specific Logback version with {@code -Dlogback.version=...}.
 *
 * <p>Test data is fictitious: {@code 123.456.789-09} is a CPF built by the check-digit algorithm
 * from the base {@code 123456789} and is the example used in the specification.
 */
class ImplicitThrowablePocTest {

    private static final String CPF = "123.456.789-09";
    private static final String CPF_REGEX = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
    private static final String CPF_MASK = "***.***.***-**";

    private LoggerContext context;
    private ByteArrayOutputStream output;

    @BeforeEach
    void createContext() {
        context = new LoggerContext();
        context.setName("poc");
        context.setMDCAdapter(new LogbackMDCAdapter());
        context.start();
        output = new ByteArrayOutputStream();
    }

    @AfterEach
    void stopContext() {
        context.stop();
    }

    @Test
    void reportsTheLogbackVersionUnderTest() {
        String expected = System.getProperty("poc.logback.version");
        String jar =
                LoggerContext.class.getProtectionDomain().getCodeSource().getLocation().toString();
        System.out.println("[PoC] logback-classic loaded from: " + jar);
        if (expected != null) {
            assertTrue(
                    jar.contains("logback-classic-" + expected + ".jar"),
                    "expected logback-classic " + expected + " on the test path");
        }
    }

    @Test
    void patternWithoutExAppendsTheThrowableImplicitly() {
        String out = render(patternEncoder("%msg%n"));

        assertTrue(out.startsWith("payment rejected for customer " + CPF), out);
        assertTrue(out.contains("java.lang.IllegalStateException: invalid document " + CPF), out);
        assertTrue(out.contains("\tat "), "stack frames are printed: " + out);
    }

    @Test
    void replaceOnMsgDoesNotMaskTheImplicitThrowable() {
        String out = render(patternEncoder("%replace(%msg){'" + CPF_REGEX + "', '" + CPF_MASK + "'}%n"));

        String firstLine = out.substring(0, out.indexOf('\n'));
        assertEquals("payment rejected for customer " + CPF_MASK, firstLine);
        assertTrue(
                out.contains("java.lang.IllegalStateException: invalid document " + CPF),
                "the CPF in the exception message is still printed in clear: " + out);
    }

    @Test
    void nopexSuppressesTheImplicitThrowable() {
        String out = render(patternEncoder("%msg%n%nopex"));

        assertEquals("payment rejected for customer " + CPF + System.lineSeparator(), out);
    }

    @Test
    void replaceWrappingExMasksTheThrowableAndSuppressesTheImplicitOne() {
        String out =
                render(patternEncoder("%replace(%msg%n%ex){'" + CPF_REGEX + "', '" + CPF_MASK + "'}"));

        assertFalse(out.contains(CPF), "no CPF in clear anywhere: " + out);
        assertTrue(out.contains("invalid document " + CPF_MASK), out);
        assertEquals(
                1,
                count(out, "java.lang.IllegalStateException"),
                "the throwable is printed once: a throwable converter inside a composite counts, so"
                        + " PatternLayout does not append its own: "
                        + out);
    }

    @Test
    void jsonEncoderWritesTheEventFieldsWithoutAnyPatternLayout() {
        JsonEncoder encoder = new JsonEncoder();
        encoder.setContext(context);
        encoder.start();

        String out = render(encoder);

        // The JSON encoder writes the raw template and the arguments separately: masking a
        // rendered line could never reach these fields.
        assertTrue(out.contains("\"message\":\"payment rejected for customer {}\""), out);
        assertTrue(out.contains("\"arguments\": [\"" + CPF + "\"]"), out);
        assertTrue(out.contains("\"message\":\"invalid document " + CPF + "\""), out);
    }

    private PatternLayoutEncoder patternEncoder(String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(pattern);
        encoder.start();
        return encoder;
    }

    private String render(Encoder<ILoggingEvent> encoder) {
        OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<>();
        appender.setContext(context);
        appender.setName("memory");
        appender.setEncoder(encoder);
        appender.setOutputStream(output);
        appender.start();

        Logger logger = context.getLogger("poc");
        logger.addAppender(appender);
        logger.error(
                "payment rejected for customer {}",
                CPF,
                new IllegalStateException("invalid document " + CPF));
        appender.stop();
        logger.detachAppender(appender);

        return output.toString(StandardCharsets.UTF_8);
    }

    private static int count(String haystack, String needle) {
        int count = 0;
        for (int i = haystack.indexOf(needle); i >= 0; i = haystack.indexOf(needle, i + 1)) {
            count++;
        }
        return count;
    }
}
