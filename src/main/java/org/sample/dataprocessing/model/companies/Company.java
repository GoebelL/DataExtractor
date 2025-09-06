package org.sample.dataprocessing.model.companies;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import org.sample.dataprocessing.BooleanConverter;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.validation.Verify;

/**
 * Author @goebel
 */
public class Company extends DataRecord {
    public final static String ID = "FI";

    @CsvBindByPosition(position = 2)
    @Verify(maxLenght = 50)
    String companyName;

    @CsvBindByPosition(position = 3)
    @Verify(maxLenght = 50)
    String street;

    @CsvBindByPosition(position = 4)
    @Verify(maxLenght = 20)
    String postBox;

    @CsvBindByPosition(position = 5)
    @Verify(maxLenght = 2)
    String isoCode;

    @CsvBindByPosition(position = 6)
    @Verify(maxLenght = 10)
    String postalCode;

    @CsvBindByPosition(position = 7)
    @Verify(maxLenght = 50)
    String city;

    @CsvCustomBindByName(column = "isDeleted", converter = BooleanConverter.class)
    @Verify(maxLenght = 2)
    boolean isDeleted;

    @CsvCustomBindByName(column = "isOEM", converter = BooleanConverter.class)
    @Verify(maxLenght = 2)
    boolean isOEM;

    @CsvBindByPosition(position = 10)
    @Verify(maxLenght = 11)
    String dunsNr;

    public String getCompanyName() {
        return companyName;
    }

    public String getStreet() {
        return street;
    }

    public String getPostBox() {
        return postBox;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isOEM() {
        return isOEM;
    }

    public String getDunsNr() {
        return dunsNr;
    }

    @SuppressWarnings("boxing")
	@Override
    public String toString() {
        return String.format("%s, %s", getCompanyName(), getNodeId());
        //return String.format("%s, %s, %s, %s, %s, %s, %s, %s", getCompanyName(), getStreet(), getPostBox(), getIsoCode(), getPostalCode(),getCity(), isDeleted(), getDunsNr());
    }
}
