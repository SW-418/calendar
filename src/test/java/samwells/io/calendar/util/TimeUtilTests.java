package samwells.io.calendar.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samwells.io.calendar.exception.InvalidDateTimeFormat;

import java.time.Instant;
import java.time.ZonedDateTime;

public class TimeUtilTests {
    private TimeUtil sut;
    private StringUtil stringUtilMock;

    @BeforeEach
    void setup() {
        stringUtilMock = Mockito.mock(StringUtil.class);
        sut = new TimeUtil(stringUtilMock);
    }

    @Test
    void convertTimestamp_withNullTimestamp_throwsInvalidDateTimeFormat() {
        String nullString = null;
        Mockito.when(stringUtilMock.isNullOrEmpty(nullString)).thenReturn(true);

        Assertions.assertThrows(InvalidDateTimeFormat.class, () -> sut.convertTimestamp(nullString));
    }

    @Test
    void convertTimestamp_withValidUtcTimestamp_throwsInvalidDateTimeFormat() {
        String validUtcTimestamp = "2025-09-25T12:00:00Z";
        Mockito.when(stringUtilMock.isNullOrEmpty(validUtcTimestamp)).thenReturn(false);

        Instant actualTimestamp = Assertions.assertDoesNotThrow(() -> sut.convertTimestamp(validUtcTimestamp));

        Assertions.assertEquals(validUtcTimestamp, actualTimestamp.toString());
    }

    @Test
    void convertTimestamp_withValidTimestampWithOffset_parsesTimeSuccessfully() {
        String validOffsetTimestamp = "2025-09-25T12:00:00-08:00";
        Mockito.when(stringUtilMock.isNullOrEmpty(validOffsetTimestamp)).thenReturn(false);

        Instant actualTimestamp = Assertions.assertDoesNotThrow(() -> sut.convertTimestamp(validOffsetTimestamp));

        Assertions.assertEquals(Instant.parse(validOffsetTimestamp).toString(), actualTimestamp.toString());
    }

    @Test
    void convertTimestamp_withValidTimestampWithZone_parsesTimeSuccessfully() {
        String validOffsetTimestamp = "2025-09-25T12:00:00-08:00[America/Los_Angeles]";
        Mockito.when(stringUtilMock.isNullOrEmpty(validOffsetTimestamp)).thenReturn(false);

        Instant actualTimestamp = Assertions.assertDoesNotThrow(() -> sut.convertTimestamp(validOffsetTimestamp));

        Assertions.assertEquals(
                ZonedDateTime.parse(validOffsetTimestamp).toInstant().toString(),
                actualTimestamp.toString()
        );
    }
}
