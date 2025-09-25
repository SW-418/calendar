package samwells.io.calendar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

public record CreateEventDto(
        @NotNull @Length(min = 1, max = 250)
        String title,
        @NotNull
        // TODO: Validation - Needs to be UTC or ZonedDateTime
        String startTime,
        @Min(0)
        int duration,
        // TODO: Add enum validation
        @NotNull
        ChronoUnit durationUnit,
        Set<Long> participants
) {
}
