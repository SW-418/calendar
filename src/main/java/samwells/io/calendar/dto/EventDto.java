package samwells.io.calendar.dto;

import samwells.io.calendar.entity.Event;
import samwells.io.calendar.entity.User;

import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

public record EventDto(
        Long id,
        String title,
        String startTime,
        String endTime,
        int duration,
        ChronoUnit durationUnit,
        Long ownerId,
        Set<Long> participants,
        String createdAt,
        String updatedAt
) {
    public EventDto(Event event) {
        this(
                event.getId(),
                event.getTitle(),
                event.getStartTime().toString(),
                event.getEndTime().toString(),
                event.getDuration(),
                event.getDurationUnit(),
                event.getOwner().getId(),
                event.getParticipants().stream().map(User::getId).collect(Collectors.toUnmodifiableSet()),
                event.getCreatedAt().toString(),
                event.getUpdatedAt().toString()
        );
    }
}
