package samwells.io.calendar.dto;

import jakarta.validation.constraints.Min;

import java.time.temporal.ChronoUnit;

public record EventUpdateDto(
        String title,
        String startTime,
        @Min(0)
        Integer duration,
        ChronoUnit durationUnit
) { }
