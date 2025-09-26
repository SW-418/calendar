package samwells.io.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.entity.User;
import samwells.io.calendar.exception.NotFoundException;
import samwells.io.calendar.repository.EventRepository;
import samwells.io.calendar.util.StringUtil;
import samwells.io.calendar.util.TimeUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final TimeUtil timeUtil;
    private final StringUtil stringUtil;

    public EventServiceImpl(EventRepository eventRepository, UserService userService, TimeUtil timeUtil, StringUtil stringUtil) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.timeUtil = timeUtil;
        this.stringUtil = stringUtil;
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
    public List<Event> getEvents(String startTime, String endTime) {
        User currentUser = getUser();
        if (stringUtil.isNullOrEmpty(startTime) && stringUtil.isNullOrEmpty(endTime)) return eventRepository.getEventsForUser(currentUser.getId());

        Instant startTimeUtc = stringUtil.isNullOrEmpty(startTime) ? null : timeUtil.convertTimestamp(startTime);
        Instant endTimeUtc = stringUtil.isNullOrEmpty(endTime) ? null : timeUtil.convertTimestamp(endTime);

        if (startTimeUtc != null && endTime == null) return eventRepository.getEventsForUserStartingFromTime(currentUser.getId(), startTimeUtc);
        if (startTimeUtc == null && endTime != null) return eventRepository.getEventsForUserEndingBeforeOrOnTime(currentUser.getId(), endTimeUtc);

        return eventRepository.getEventsForUser(currentUser.getId(), startTimeUtc, endTimeUtc);
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
}
