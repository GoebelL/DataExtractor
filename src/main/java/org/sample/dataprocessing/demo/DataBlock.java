package org.sample.dataprocessing.demo;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.records.RecordInfo;
import org.sample.dataprocessing.records.RecordTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of a {@code}Data Block as list of records, where the first record is the parent record followed 
 * by a list of its child records.
 * <p>
 * @see org.sample.dataprocessing.demo.DataBlockReader
 *     
 * @author goebel
 */
public class DataBlock<T extends DataRecord> {
    private static Log log = LogFactory.getLog(DataBlock.class);
    private final RecordTypes recordTypes;
    private final BufferedReader reader;
    private final CSVParser parser;

    private T nodeRecord;
    private final List<DataRecord> childRecords = new ArrayList<>();
    private final Predicate<DataRecord> doInclude;

    private final List<String> blockIds = new ArrayList<>();
    
	@SuppressWarnings("unchecked")
	DataBlock(BufferedReader reader, CSVParser parser, RecordTypes recordTypes, Predicate<? extends DataRecord> doInclude)
            throws IOException, CsvValidationException {
        this.reader = reader;
        this.parser = parser;
        this.recordTypes = recordTypes;
        this.doInclude = (Predicate<DataRecord>) doInclude;
        readIntern(reader);
    }

    public DataRecord getNodeRecord() {
        return nodeRecord;
    }

    public List<DataRecord> getChildRecords() {
        return childRecords;
    }

    private void readIntern(BufferedReader reader) throws IOException, CsvValidationException {
        while (true) {
            reader.mark(200);
            String line = readNextLine(reader);
            if (line != null) {

                final RecordInfo recordInfo = getRecordInfo(line);

                final Optional<? extends DataRecord> record = readRecord(recordInfo.getType(), recordInfo.getColumnHeaders(),
                        getLineReader(line, recordInfo.getValidator()));

                if (record.isPresent()) {
                    final String blockId = record.get().getNodeId();
                    if (!blockIds.contains(blockId)) {
                        if (blockIds.isEmpty()) {
                            // first is the parent record
                            collectParent(record.get());
                            blockIds.add(blockId);
                            continue;
                        }
                        // start of next block -> reset and break 
                        reader.reset();
                        break;
                    } else {
                        // when revisiting the node we proceed the child records
                        collectChild(record.get());
                        continue;
                    }

                }
                reader.reset();
            }
            break;
        }
    }

    @SuppressWarnings("unchecked")
	private void collectParent(DataRecord rec) {
        nodeRecord = (T) rec;
    }

    private void collectChild(DataRecord rec) {
        if (doInclude.test(rec)) {
            childRecords.add(rec);
        }
    }

    CSVReader getLineReader(String line, LineValidator validator) {
        try {
        	validator.validate(line);
        } catch (CsvValidationException e) {
            log.warn(e);
        }
        return new CSVReaderBuilder(new StringReader(line))
        		.withCSVParser(parser).build();
    }

    RecordInfo getRecordInfo(String line) {
        final String[] csv = line.split("\t");
        if (csv.length < 1) {
            throw new RuntimeException(
                    String.format("Failed to parse data file: Expected a tab separated record, but found '%s')", line));
        }
        return Optional.ofNullable(recordTypes.get(csv[0])).orElseThrow(() ->
                new RuntimeException(String.format("Unknown Record Idenitfier '%s')", csv[0])));

    }

   <R> Optional<R> readRecord(Class<R> type, String[] headers, CSVReader reader) {
        ColumnPositionMappingStrategy<R> strategy = new ColumnPositionMappingStrategy<R>();
        strategy.setType(type);
        strategy.setColumnMapping(headers);

        CsvToBean<R> dataRecords = new CsvToBeanBuilder<R>(reader).withType(type).withSeparator('\t')
                .withMappingStrategy(strategy).build();
        final Iterator<R> recordsIterator = dataRecords.iterator();
        if (recordsIterator.hasNext()) {
            return Optional.of(recordsIterator.next());
        }
        return Optional.empty();
    }

    static String readNextLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while (line != null && line.trim().startsWith("#")) {
            line = reader.readLine();
        }
        return fixFormatForParser(line);
    }

    private static String fixFormatForParser(String line) {
        // To avoid parser error due to unterminated double quotes (occurs in Substances.dat),
        // we simply escape them with another double quotes.
        if (line!=null) {
            line = line.replace("\"", "\"\"");
        }
        return line;
    }

    @Override
    public String toString() {
        if (nodeRecord == null) {
            return "[null]";
        }
        return String.format("%s : %s", nodeRecord.getType(), nodeRecord.getNodeId());
    }

}
