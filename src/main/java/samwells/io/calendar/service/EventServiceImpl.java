package samwells.io.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.entity.User;
import samwells.io.calendar.exception.InvalidDateTimeFormat;
import samwells.io.calendar.exception.NotFoundException;
import samwells.io.calendar.repository.EventRepository;
import samwells.io.calendar.util.TimeUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final TimeUtil timeUtil;
    private final int PAGE_SIZE = 2;

    public EventServiceImpl(EventRepository eventRepository, UserService userService, TimeUtil timeUtil) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.timeUtil = timeUtil;
    }

    @Override
    @Transactional
    public Event createEvent(String title, String startTime, int duration, ChronoUnit durationUnit, Set<Long> participants) {
        // Validate and create UTC start + end times
        Instant utcStart = timeUtil.convertTimestamp(startTime);
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
    public List<Event> getEvents(String startTime, String endTime, String lastStartTime, Long lastId) {
        User currentUser = getUser();

        // Only get future events if no start time provided
        Instant sanitizedStartTimeUtc = Objects.requireNonNullElse(getSanitizedTimestamp(startTime), Instant.now());
        Instant sanitizedEndTimeUtc = getSanitizedTimestamp(endTime);
        Instant sanitizedLastStartTime = getSanitizedTimestamp(lastStartTime);
        PageRequest pageRequest = PageRequest.ofSize(PAGE_SIZE);

        return eventRepository.getEventsForUser(
                currentUser.getId(),
                sanitizedStartTimeUtc,
                sanitizedEndTimeUtc,
                sanitizedLastStartTime,
                lastId,
                pageRequest
        );
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

    @Override
    @Transactional
    public Event updateEvent(Long eventId, String title, String startTime, Integer duration, ChronoUnit durationUnit) {
        // Gets event for current user
        Event event = getEvent(eventId);

        if (title != null && !title.isBlank()) event.setTitle(title);
        if (startTime != null && !startTime.isBlank()) event.setStartTime(timeUtil.convertTimestamp(startTime));
        if (duration != null) event.setDuration(duration);
        if (durationUnit != null) event.setDurationUnit(durationUnit);

        event.setEndTime(event.getStartTime().plus(event.getDuration(), event.getDurationUnit()));

        eventRepository.save(event);

        return event;
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (User) auth.getPrincipal();
    }

    private Instant getSanitizedTimestamp(String timestamp) {
        try {
            return timeUtil.convertTimestamp(timestamp);
        } catch (InvalidDateTimeFormat | NullPointerException ex) {
            return null;
        }
    }
}
