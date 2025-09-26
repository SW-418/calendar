package samwells.io.calendar.mapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import samwells.io.calendar.dto.EventDto;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.entity.User;

import java.time.ZoneId;
import java.util.stream.Collectors;

@Component
public class EventToEventDtoMapper implements Mapper<Event, EventDto> {
    @Override
    public EventDto map(Event event) {
        User user = getUser();
        String timezonePreference = user.getTimezonePreference();

        if (timezonePreference == null || timezonePreference.isBlank()) return new EventDto(event);
        ZoneId zone = ZoneId.of(timezonePreference);

        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getStartTime().atZone(zone).toString(),
                event.getEndTime().atZone(zone).toString(),
                event.getDuration(),
                event.getDurationUnit(),
                event.getOwner().getId(),
                event.getParticipants().stream().map(User::getId).collect(Collectors.toUnmodifiableSet()),
                event.getCreatedAt().toString(),
                event.getUpdatedAt().toString()
        );
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (User) auth.getPrincipal();
    }
}
