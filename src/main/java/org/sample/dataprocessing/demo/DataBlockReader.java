package org.sample.dataprocessing.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Predicate;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.records.RecordTypes;

/**
 * DataBlockerReader is an {@link Iterator} implementation for iteratively parsing {@link DataBlock}s from given CSV data. It reads data
 * block-wise from input data provided in a specific tab separated form.
 * <p>
 * @see DataBlock
 * 
 * @author goebel
 */
public class DataBlockReader<T extends DataRecord> implements Iterator<DataBlock<T>> {

	private CSVParser parser;
	private RecordTypes recordTypes;
	private BufferedReader reader;
	private Predicate<? extends DataRecord> doInclude;

	/**
	 * DataBlockerReader constructor from a given <code>BufferedReader<code>.
	 * 
	 * @param reader      - an instance of {@link} BufferedReader
	 * @param recordTypes - an instance of <code>RecordTypes<code>, that provides
	 *                    information of contained records and their child records
	 *                    @see 
	 * @param <C>         - type of child record
	 * @param doInclude   - a filter predicate for filtering child records from
	 *                    blocks
	 */
	@SuppressWarnings("unchecked")
	public <C> DataBlockReader(BufferedReader reader, RecordTypes recordTypes, Predicate<C> doInclude) {
		this.recordTypes = recordTypes;
		this.reader = reader;
		this.doInclude = (Predicate<? extends DataRecord>) doInclude;
		parser = new CSVParserBuilder().withSeparator('\t').build();
	}

	@Override
	public boolean hasNext() {
		try {
			reader.mark(200);
			try {
				return DataBlock.readNextLine(reader) != null;
			} finally {
				reader.reset();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DataBlock<T> next() {
		try {
			return new DataBlock<T>(reader, parser, recordTypes, doInclude);
		} catch (IOException | CsvValidationException e) {
			throw new RuntimeException(e);
		}
	}
}