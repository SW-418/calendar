package samwells.io.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.entity.User;
import samwells.io.calendar.exception.InvalidDateTimeFormat;
import samwells.io.calendar.exception.NotFoundException;
import samwells.io.calendar.repository.EventRepository;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Event createEvent(String title, String startTime, int duration, ChronoUnit durationUnit, Set<Long> participants) {
        // Validate and create UTC start + end times
        Instant utcStart = convertTimestamp(startTime);
        Instant utcEnd = utcStart.plus(duration, durationUnit);

        User currentUser = getUser();

        Event newEvent = new Event(
                title,
                utcStart,
                utcEnd,
                duration,
                durationUnit,
                currentUser
        );

        // TODO: Validation around user existence - Probably want to return an error if a user doesn't exist
        var users = new HashSet<>(userService.getUsersById(participants));
        users.add(currentUser);
        newEvent.setParticipants(users);

        return eventRepository.save(newEvent);
    }

    @Override
    public List<Event> getEvents() {
        User currentUser = getUser();
        return eventRepository.getEventsForUser(currentUser.getId());
    }

    @Override
    public Event getEvent(Long eventId) {
        return eventRepository
                .getEventForUser(eventId, getUser().getId())
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        int affectedRows = eventRepository.deleteEventForUser(eventId, getUser().getId());
        if (affectedRows == 0) throw new NotFoundException(eventId);
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (User) auth.getPrincipal();
    }

    // TODO: Single responsibility refactor
    private Instant convertTimestamp(String timestamp) {
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

        throw new InvalidDateTimeFormat();
    }
}
