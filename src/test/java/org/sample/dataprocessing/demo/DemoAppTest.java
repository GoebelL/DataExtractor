package org.sample.dataprocessing.demo;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sample.dataprocessing.model.substances.Substance;
import org.sample.dataprocessing.records.RecordInfoBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author goebel
 */
public class DemoAppTest {

    private PrintStream stdout;
    private Locale defaultLocale;

    @BeforeEach
    public void getSystemIn() {
        stdout = System.out;
        defaultLocale = Locale.getDefault();
    }

    @AfterEach
    public void resetSystemIn() {
        Locale.setDefault(defaultLocale);
        System.setOut(stdout);
    }


    @Test
    void testPrintSubstances_english() throws URISyntaxException, IOException {
        // TODO Should actually be an integration test
        // given
        givenDefaultLanguage("en");
        setExpectedOutput("22222-22-2, TestE");

        Path substancesFile = givenSubstancesFile();

        // when
        new DemoApp().printSubstances(substancesFile);

        // then
        assertWrittenToConsole("22222-22-2, TestE");
    }

    @Test
    void testPrintSubstances_german() throws URISyntaxException, IOException {

        // given
        givenDefaultLanguage("de");
        setExpectedOutput("22222-22-2, TestD");

        Path substancesFile = givenSubstancesFile();

        // when
        new DemoApp().printSubstances(substancesFile);

        // then
        assertWrittenToConsole("22222-22-2, TestD");
    }

    private static void waitForConsole(long sec) {
        // make sure we have written all!
        System.out.flush();
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testPrintCompanies() throws URISyntaxException, IOException {
        // given
        setExpectedOutput("BSDA TecSoft");

        final Path companiesFile = givenCompaniesFile();

        // when
        new DemoApp().printCompanies(companiesFile);

        // then
        assertWrittenToConsole("BSDA TecSoft");
    }

    @Test
    void getColumnheaders() {

        // given
        final String[] givenHeaders = {"type", "nodeId", "casCode", "euIndexCode", "einecsElincsCode", "dutyToDeclare", "isUnwanted", "isProhibitted",
                "isReach", "isDeleted", "isHidden"};

        // when
        String[] substanceHeaders = RecordInfoBuilder.build(Substance.class).getColumnHeaders();

        // then
        assertArrayEquals(givenHeaders, substanceHeaders);

    }

    private Path givenSubstancesFile() throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("Substances.dat").toURI());
    }

    private Path givenCompaniesFile() throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("Companies.dat").toURI());
    }

    private void setExpectedOutput(String expectedOutput) {
        System.setOut(new ConsoleOutput(System.out, expectedOutput));
    }

    private void assertWrittenToConsole(String mesage) {
        assertWrittenToConsole(mesage, 2);
    }

    private void assertWrittenToConsole(String message, int wait) {
        waitForConsole(wait);
        assertThat(capturedMessage(), containsString(message));
    }

    private String capturedMessage() {
        return ((ConsoleOutput) System.out).captured;
    }

    static class ConsoleOutput extends PrintStream {
        private PrintStream ps;

        private String captured = "";

        private final String expectedOutput;

        ConsoleOutput(OutputStream os, String expectedOutput) {
            super(os, true);
            this.expectedOutput = expectedOutput;
        }

        @Override
        public PrintStream printf(String format, Object... args) {
            checkOutput(format, args);
            return super.printf(format, args);
        }

        private void checkOutput(String format, Object... args) {
            String msg = String.format(format, args);
            if (msg.contains(expectedOutput)) {
                captured = msg;
            }
        }
    }

    void givenDefaultLanguage(String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        Locale.setDefault(locale);
    }
}
