/*
 ******************************************************************************

 <p>Copyright FUJITSU LIMITED 2022

 <p>*****************************************************************************
*/
package org.sample.dataprocessing.records;

import org.sample.dataprocessing.model.substances.Substance;
import org.sample.dataprocessing.validation.Verify;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * Author @goebel
 */
public class RecordInfoTest {

    @Test
    public void getHeaders() {
        // given
        RecordInfo ri = RecordInfoBuilder.build(Substance.class);

        final String[] givenHeaders = {"type", "nodeId", "casCode", "euIndexCode", "einecsElincsCode", "dutyToDeclare", "isUnwanted", "isProhibitted",
                "isReach", "isDeleted", "isHidden"};

        // when
        String[] headers = ri.getColumnHeaders();

        // then
        Arrays.stream(headers).forEach(System.out::println);
        System.out.println();
        Arrays.stream(givenHeaders).forEach(System.out::println);
        assertArrayEquals(givenHeaders, headers);
    }

    @SuppressWarnings("boxing")
    @Test
    public void getMaxColumnLength() {
        // given
        RecordInfo ri = RecordInfoBuilder.build(Substance.class);

        final int[] givenHeaderLengths = {2, 18, 20, 20, 20, 1, 2, 1, 1, 1, 1};

        // when
        int[] headerLengths = ri.getMaxColumnLength();

        // then
        Arrays.stream(ri.getType().getDeclaredFields()).map(f -> String.format("%s - length: %s", f.getName(), maxLen(f))).forEach(System.out::println);
        assertArrayEquals(givenHeaderLengths, headerLengths);
    }

    @SuppressWarnings("boxing")
    private int maxLen(Field f) {
        return Optional.ofNullable(f.getAnnotation(Verify.class)).map(Verify::maxLenght).orElse(20);
    }
}
