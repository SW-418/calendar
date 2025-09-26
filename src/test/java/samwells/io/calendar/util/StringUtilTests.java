package samwells.io.calendar.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StringUtilTests {
    private StringUtil sut;

    @BeforeEach
    void setUp() {
        sut = new StringUtil();
    }

    @Test
    void isNullOrEmpty_withNullString_returnsTrue() {
        String nullString = null;

        Assertions.assertTrue(sut.isNullOrEmpty(nullString));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " })
    void isNullOrEmpty_withEmptyString_returnsTrue(String s) {
        Assertions.assertTrue(sut.isNullOrEmpty(s));
    }

    @ParameterizedTest
    @ValueSource(strings = { "s", " aaaaa", "aaaaa", "aaaaa    ", " 1 " })
    void isNullOrEmpty_withString_returnsFalse(String s) {
        Assertions.assertFalse(sut.isNullOrEmpty(s));
    }
}
