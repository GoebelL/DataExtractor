/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.sample.dataprocessing.records;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.sample.dataprocessing.validation.DataRecordValidator;
import org.sample.dataprocessing.validation.Verify;

/**
 * Factory for <code>RecordInfo</code>.
 * <p>
 * Author @goebel
 */
public final class RecordInfoBuilder {

    public static RecordInfo build(Class<? extends DataRecord> type) {
        RecordInfo info = new RecordInfo(type);
        info.setColumnHeaders(buildColumnHeaders(type));
        info.setMaxFieldLengths(buildMaxLengths(type));
        info.setValidator(new DataRecordValidator(info));
        return info;
    }

    private static String[] buildColumnHeaders(Class<? extends DataRecord> type) {
        List<String> headers = Stream.of(DataRecord.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        // Use declared fields on record type as headers but ignore the ID field
        headers.addAll(Stream.of(type.getDeclaredFields()).filter(fi -> !"ID".equalsIgnoreCase(fi.getName())).map(Field::getName).
                collect(Collectors.toList()));

        return  headers.toArray(String[]::new);
    }

    @SuppressWarnings("boxing")
	private static int[] buildMaxLengths(Class<? extends DataRecord> type) {
        IntStream.Builder intStream = IntStream.builder();
        // Process common fields, read length of all declared DataRecord fields
        for (Field f : DataRecord.class.getDeclaredFields()) {
            Optional<Integer> maxLen = Optional.ofNullable(f.getAnnotation(Verify.class)).map(Verify::maxLenght);
            intStream.add(maxLen.orElseGet(() -> DataRecordValidator.DEFAULT_FIELD_LENGTH));
        }
        // Get length of all declared fields on type, but ignore the ID field (already handled)
        Arrays.stream(type.getDeclaredFields()).filter(fi -> !"ID".equalsIgnoreCase(fi.getName())).
                forEach(f -> {
                            Optional<Integer> maxLen = Optional.ofNullable(f.getAnnotation(Verify.class)).map(Verify::maxLenght);
                            intStream.add(maxLen.orElseGet(() -> DataRecordValidator.DEFAULT_FIELD_LENGTH));
                        }
                );
        return intStream.build().toArray();
    }
}
