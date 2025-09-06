package org.sample.dataprocessing.model.companies;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import org.sample.dataprocessing.BooleanConverter;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.validation.Verify;

/**
 * Author @goebel
 */
public class CompUnits extends DataRecord {
    public final static String ID = "FE";

    @Verify(maxLenght = 9)
    @CsvBindByPosition(position = 2)
    String orgUnitId;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 3)
    String orgUnitName;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 4)
    String orgUnitStreet;

    @Verify(maxLenght = 20)
    @CsvBindByPosition(position = 5)
    String orgUnitPostbox;

    @Verify(maxLenght = 2)
    @CsvBindByPosition(position = 6)
    String orgUnitIsoCode;
    
 
    public String getOrgUnitCity() {
        return orgUnitCity;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getDunsNr() {
        return dunsNr;
    }

    @Verify(maxLenght = 10)
    @CsvBindByPosition(position = 7)
    String orgUnitPostalCode;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 8)
    String orgUnitCity;

    @Verify(maxLenght = 1)
    @CsvCustomBindByName(column = "isDeleted", converter = BooleanConverter.class)
    boolean isDeleted;

    @Verify(maxLenght = 11)
    @CsvBindByPosition(position = 10)
    String dunsNr;

    public String getOrgUnitId() {
        return orgUnitId;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public String getOrgUnitStreet() {
        return orgUnitStreet;
    }

    public String getOrgUnitPostbox() {
        return orgUnitPostbox;
    }

    public String getOrgUnitIsoCode() {
        return orgUnitIsoCode;
    }

    public String getOrgUnitPostalCode() {
        return orgUnitPostalCode;
    }


    @SuppressWarnings("boxing")
	@Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", getOrgUnitId(), getOrgUnitName(), getOrgUnitStreet(), getOrgUnitPostbox(), getOrgUnitIsoCode(), getOrgUnitPostalCode(), getOrgUnitCity(), isDeleted(), getDunsNr());
    }
}
