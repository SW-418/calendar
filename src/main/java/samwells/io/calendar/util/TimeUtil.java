package samwells.io.calendar.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import samwells.io.calendar.exception.InvalidDateTimeFormat;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
public class TimeUtil {
    private final StringUtil stringUtil;

    public TimeUtil(StringUtil stringUtil) {
        this.stringUtil = stringUtil;
    }

    public Instant convertTimestamp(String timestamp) {
        if (stringUtil.isNullOrEmpty(timestamp)) throw new InvalidDateTimeFormat();

        try {
            return Instant.parse(timestamp);
        } catch (DateTimeParseException exception) {
            log.warn("Couldn't parse timestamp directly to UTC - {}", timestamp);
        }

        try {
            return ZonedDateTime.parse(timestamp).toInstant();
        } catch (DateTimeParseException exception) {
            log.warn("Couldn't parse timestamp to UTC using zoning - {}", timestamp);
        }

        try {
            return OffsetDateTime.parse(timestamp).toInstant();
        } catch (DateTimeParseException exception) {
            log.warn("Couldn't parse timestamp to UTC using offset - {}", timestamp);
        }

        throw new InvalidDateTimeFormat();
    }
}
