package org.sample.dataprocessing;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

/**
 * @author goebel
 */
@SuppressWarnings("rawtypes")
public class BooleanConverter extends AbstractBeanField {

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		final String val = value.trim();
		if (val.length() == 1) {
			return Boolean.valueOf(1 == Integer.valueOf(val).intValue());
		}
		return Boolean.FALSE;
	}
}
