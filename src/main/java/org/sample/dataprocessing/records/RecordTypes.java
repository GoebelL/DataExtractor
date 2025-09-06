package org.sample.dataprocessing.records;

import java.util.Map;

/**
 * Mapping of record identifiers and types required generic data processing.
 * <p>
 * @see RecordInfo
 * 
 * @author goebel
 *
 */
public class RecordTypes {
	
	/**
	 * Constructor from identifiers to types map. 
	 * <p>
	 * @param recordTypes - given record type
	 * 
	 * @see RecordInfo
	 */
	public RecordTypes(Map<String, RecordInfo> recordTypes) {
		recordIds2Types = recordTypes;	
	}

	private Map<String, RecordInfo> recordIds2Types;
	
	public RecordInfo get(String id) {
		return recordIds2Types.get(id);
	}
}
