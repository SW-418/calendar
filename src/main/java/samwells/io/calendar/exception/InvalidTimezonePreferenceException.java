package samwells.io.calendar.exception;

public class InvalidTimezonePreferenceException extends RuntimeException {
    public InvalidTimezonePreferenceException(String timezonePreference) {
        super(String.format("%s is not a supported timezone preference - The preference should be in the format {country}/{city}", timezonePreference));
    }
}
