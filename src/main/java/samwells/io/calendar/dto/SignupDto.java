package samwells.io.calendar.dto;

public record SignupDto(
        String email,
        String password,
        String firstname,
        String surname,
        String timezonePreference
) {
}
