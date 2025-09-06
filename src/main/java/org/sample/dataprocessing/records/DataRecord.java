package org.sample.dataprocessing.records;

import com.opencsv.bean.CsvBindByPosition;
import org.sample.dataprocessing.validation.Verify;

/**
 * @author goebel
 *
 */
public class DataRecord {

	@CsvBindByPosition(position = 0)
	@Verify(maxLenght = 2)
	protected String type;
	
	@CsvBindByPosition(position = 1)
	@Verify(maxLenght = 18)
	protected String nodeId;

	public String getType() {
		return type;
	}
	
	public String getNodeId() {
		return nodeId;
	}
}
