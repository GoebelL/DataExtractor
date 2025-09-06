package org.sample.dataprocessing.model.substances;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;

import org.sample.dataprocessing.BooleanConverter;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.validation.Verify;

/**
 * @author goebel
 *
 */
public class Substance extends DataRecord {

	@Verify(maxLenght = 1)
	public final static  String ID = "R";

	@CsvBindByPosition(position = 2)
	@Verify(maxLenght = 20)
	private String casCode;

	@CsvBindByPosition(position = 3)
	@Verify(maxLenght = 20)
	private String euIndexCode;

	@CsvBindByPosition(position = 4)
	@Verify(maxLenght = 20)
	private String einecsElincsCode;

	@CsvCustomBindByName(column = "dutyToDeclare", converter = BooleanConverter.class)
	@Verify(maxLenght = 1)
	private boolean dutyToDeclare;

	@CsvCustomBindByName(column = "isUnwanted", converter = BooleanConverter.class)
	@Verify(maxLenght = 2)
	private boolean isUnwanted;

	@CsvCustomBindByName(column = "isProhibitted", converter = BooleanConverter.class)
	@Verify(maxLenght = 1)
	private boolean isProhibitted;

	@CsvCustomBindByName(column = "isReach", converter = BooleanConverter.class)
	@Verify(maxLenght = 1)
	private boolean isReach;

	@CsvCustomBindByName(column = "isDeleted", converter = BooleanConverter.class)
	@Verify(maxLenght = 1)
	private boolean isDeleted;

	@CsvCustomBindByName(column = "isHidden", converter = BooleanConverter.class)
	@Verify(maxLenght = 1)
	private boolean isHidden;

	public String getCasCode() {
		return casCode;
	}

	public String getEuIndexCode() {
		return euIndexCode;
	}

	public String getEinecsElincsCode() {
		return einecsElincsCode;
	}

	public boolean isDutyToDeclare() {
		return dutyToDeclare;
	}

	public boolean isUnwanted() {
		return isUnwanted;
	}

	public boolean isProhibitted() {
		return isProhibitted;
	}

	public boolean isReach() {
		return isReach;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public boolean isHidden() {
		return isHidden;
	}

	@Override
	public String toString() {
		return getCasCode();
	}
}