package samwells.io.calendar.exception;

public class InvalidDateTimeFormat extends RuntimeException {
    public InvalidDateTimeFormat() {
        super("Timestamps must be in UTC format or contain TimeZone");
    }
}
