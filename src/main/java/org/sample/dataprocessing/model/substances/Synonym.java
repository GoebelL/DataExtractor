package org.sample.dataprocessing.model.substances;

import com.opencsv.bean.CsvBindByPosition;

import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.validation.Verify;

/**
 * @author goebel
 */
public class Synonym extends DataRecord {
	@Verify(maxLenght = 2)
	public final static String ID = "RN";
	@CsvBindByPosition(position = 2)
	@Verify(maxLenght = 18)
	private String synonymId;
	
	@CsvBindByPosition(position = 3)
	@Verify(maxLenght = 2)
	private String isoLanguage;
	
	@CsvBindByPosition(position = 4)
	@Verify(maxLenght = 250)
	private String synonymName;

	public String getSynonymId() {
		return synonymId;
	}

	public String getIsoLanguage() {
		return isoLanguage;
	}

	public String getSynonymName() {
		return synonymName;
	}

	@Override
	public String toString() {
		return getSynonymName();
	}
}
