package org.sample.dataprocessing.records;

import com.opencsv.validators.LineValidator;

/**
 * Representation of given record type.
 * <p>
 * @author goebel
 */
public class RecordInfo {

    private String[] headers;
    private Class<? extends DataRecord> type;
    private int[] maxFieldLengths;
    private LineValidator dataRecordValidator;

    RecordInfo(Class<? extends DataRecord> type) {
        this.type = type;
    }

    public Class<? extends DataRecord> getType() {
        return type;
    }

    public LineValidator getValidator() {
        return dataRecordValidator;
    }

    public void setColumnHeaders(String[] headers) {
        this.headers = headers;
    }

    public String[] getColumnHeaders() {
        return headers;
    }

    public int[] getMaxColumnLength() {
        return maxFieldLengths;
    }

    void setMaxFieldLengths(int[] lengths) {
        this.maxFieldLengths = lengths;
    }

    void setValidator(LineValidator dataRecordValidator) {
        this.dataRecordValidator = dataRecordValidator;
    }
}
