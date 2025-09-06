package org.sample.dataprocessing.demo;

import org.sample.dataprocessing.model.companies.CompUnits;
import org.sample.dataprocessing.model.companies.Company;
import org.sample.dataprocessing.model.companies.Contact;
import org.sample.dataprocessing.model.substances.Substance;
import org.sample.dataprocessing.model.substances.Synonym;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.records.RecordInfo;
import org.sample.dataprocessing.records.RecordInfoBuilder;
import org.sample.dataprocessing.records.RecordTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A demo printing Substances data records in given system default language.
 * 
 * @author goebel
 */
public class DemoApp {

    /**
     * Given data files are windows cp-1252 encoded. This becomes an issue when adding other languages using multi-byte encodings.
     * Instead the files could be exported with UTF-8 encoding.
     * <p>
     * For a generic approach ICU4j could be used to determine the encoding for reading the file content.
     */
    static final Charset CHARSET = Charset.forName("windows-1252");


    public static void main(String[] args) {
        try {
            // Make sure we have an existing file
            final Path dataFile = Paths.get(getFirstOrDefault(args));

            ensureExists(dataFile);

            // And import it
            new DemoApp().printSubstances(dataFile);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private RecordTypes buildRecordTypes() {
        final Map<String, RecordInfo> map = Map.of(
                Substance.ID, RecordInfoBuilder.build(Substance.class),
                Synonym.ID, RecordInfoBuilder.build(Synonym.class),
                Company.ID, RecordInfoBuilder.build(Company.class),
                CompUnits.ID, RecordInfoBuilder.build(CompUnits.class),
                Contact.ID, RecordInfoBuilder.build(Contact.class)
        );
        return new RecordTypes(map);
    }

    void printSubstances(Path path) throws URISyntaxException, IOException {

        final String lang = Locale.getDefault().getLanguage();

        printItems(path, Substance.class,filterByLanguage(lang));
    }

    void printCompanies(Path path) throws URISyntaxException, IOException {
        printItems(path, Company.class, noFilter());
    }


    private <P, C> void printItems(final Path path, Class<P> parentClass, Predicate<C> filter) throws URISyntaxException, IOException {
        // Process and output companies file block by block (with a small wrapper same code could be reused)
        try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
            final DataBlockReader<DataRecord> dr = new DataBlockReader<>(reader, buildRecordTypes(), filter);
            while (dr.hasNext()) {
                final DataBlock<DataRecord> block = dr.next();
                printDatablock(parentClass, block);
            }
        }
    }

    private Predicate<Synonym> filterByLanguage(String language) {
        return item -> item.getIsoLanguage().equalsIgnoreCase(language);
    }

    private <T> Predicate<T> noFilter() {
        return item -> true;
    }

    private static String getSubstancesFileAsDefault() throws URISyntaxException {
        return getSubstancesFile().toString();
    }

    private static Path getSubstancesFile() throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("Substances.dat").toURI());
    }

    private static void ensureExists(Path path) throws IOException {
        if (!Files.exists(path))
            throw new IllegalArgumentException(String.format("The file %s was not found. Please specify the full path.", path));
    }

    private static String getFirstOrDefault(String[] args) throws IllegalArgumentException, URISyntaxException {
        // if no argument is given we use the substances.dat as default
        Function<String[], Optional<String>> findFirstArgument = x -> Arrays.stream(x).findFirst();
        return Optional.of(args)
                .flatMap(findFirstArgument)
                .orElse(getSubstancesFileAsDefault());
    }

    private <P> void printDatablock(Class<P> parentClazz, DataBlock<? extends DataRecord> block) {
        @SuppressWarnings("unchecked")
		P parent = (P) block.getNodeRecord();
        block.getChildRecords().stream().forEach(
                child -> System.out.printf("%s, %s\n", parent, child)
        );
    }
}
