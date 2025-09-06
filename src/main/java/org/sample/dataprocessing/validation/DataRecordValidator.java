package org.sample.dataprocessing.validation;

import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;
import org.sample.dataprocessing.records.RecordInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Validates a given CSV Line.
 * <p>
 * Author @goebel
 */
public class DataRecordValidator implements LineValidator {
    private static final Log log = LogFactory.getLog(DataRecordValidator.class);

    private final RecordInfo info;

    public static int DEFAULT_FIELD_LENGTH = 20;
    public DataRecordValidator(RecordInfo info) {
        this.info = info;
    }

    public boolean isValid(String[] vals) {
        if (vals.length != info.getMaxColumnLength().length)
            return false;
        List<String> values = Arrays.asList(vals);

        // Remove correct values each by each and check if all limits have been processed
        return Arrays.stream(info.getMaxColumnLength()).filter(len ->
                {
                    final String value = values.remove(0);
                    return (len >= value.length());
                }
        ).count() == 0;
    }
    
	@SuppressWarnings("boxing")
	public void validate(String[] vals) throws CsvValidationException {
        // basic check
        if (vals.length != info.getMaxColumnLength().length) {
            log.warn(String.format("Error: %s", Arrays.stream(vals).map(s -> s + ", ").collect(Collectors.joining())));
            throw new CsvValidationException(String.format("Unexpected number of fields found in data file (expected %s nut was %s)", info.getMaxColumnLength().length, vals.length));
        }

        List<String> values = Arrays.asList(vals);
        List<String> labels = Arrays.asList(info.getColumnHeaders());

        // build a map of values and corresponding max lengths
        List<Integer> lenList = Arrays.stream(info.getMaxColumnLength()).boxed().toList();
        final Map<String, String> valuesMap = IntStream.range(0, labels.size()).boxed()
                .collect(Collectors.toMap(labels::get, values::get));

        // check lengths
        for (String label : labels) {
            final String value = valuesMap.get(label);
            final int idx = values.indexOf(value);
            final int maxLen = lenList.get(idx).intValue();
            if (value.length() > maxLen) {
                throw new CsvValidationException(String.format("The value '%s' exceeds the maximum length of %s (=%s).", value, labels.get(idx), maxLen));
            }
        }
    }

    @Override
    public boolean isValid(String s) {
        return isValid(s.split("\t"));
    }

    @Override
    public void validate(String s) throws CsvValidationException {
        validate(s.split("\t"));
    }
}
