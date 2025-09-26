package samwells.io.calendar.service;

import samwells.io.calendar.entity.Event;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public interface EventService {
    Event createEvent(
            String title,
            String startTime,
            int duration,
            ChronoUnit durationUnit,
            Set<Long> participants
    );
    List<Event> getEvents(String startTime, String endTime);
    Event getEvent(Long eventId);
    void deleteEvent(Long eventId);
    Event updateEvent(
            Long eventId,
            String title,
            String startTime,
            Integer duration,
            ChronoUnit durationUnit
    );
}
