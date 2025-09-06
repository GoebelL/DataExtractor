package org.sample.dataprocessing.model.companies;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import org.sample.dataprocessing.BooleanConverter;
import org.sample.dataprocessing.records.DataRecord;
import org.sample.dataprocessing.validation.Verify;

/**
 * Author @goebel
 */
public class Contact extends DataRecord {
    public final static String ID = "FK";

    @Verify(maxLenght = 4)
    @CsvBindByPosition(position = 2)
    String contactIdInList;

    @Verify(maxLenght = 1)
    @CsvCustomBindByName(column = "isDeleted", converter = BooleanConverter.class)
    boolean isDeleted;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 4)
    String contactLastName;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 5)
    String contactFirstName;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 6)
    String contactPhoneNr;

    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 7)
    String contactFaxNr;

    @Verify(maxLenght = 80)
    @CsvBindByPosition(position = 8)
    String contactEmailAddress;
    
    @Verify(maxLenght = 50)
    @CsvBindByPosition(position = 9)
    String departmentName;

    @Verify(maxLenght = 20)
    @CsvBindByPosition(position = 10)
    String contactMailbox;

    @Verify(maxLenght = 1)
    @CsvCustomBindByName(column = "isIMDSContact", converter = BooleanConverter.class)
    boolean isIMDSContact;

    @Verify(maxLenght = 1)
    @CsvCustomBindByName(column = "isReachContact", converter = BooleanConverter.class)
    boolean isReachContact;

    public String getContactIdInList() {
        return contactIdInList;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public String getContactPhoneNr() {
        return contactPhoneNr;
    }

    public String getContactFaxNr() {
        return contactFaxNr;
    }

    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getContactMailbox() {
        return contactMailbox;
    }

    public boolean isIMDSContact() {
        return isIMDSContact;
    }

    public boolean isReachContact() {
        return isReachContact;
    }

    @SuppressWarnings("boxing")
	@Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", getContactLastName(), getContactFirstName(), getContactIdInList(), isDeleted(), getContactPhoneNr(), getContactFaxNr(), getContactEmailAddress(), getDepartmentName(), getContactMailbox(), isIMDSContact(), isReachContact());
    }
}
