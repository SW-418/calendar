package samwells.io.calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samwells.io.calendar.dto.CreateEventDto;
import samwells.io.calendar.dto.EventDto;
import samwells.io.calendar.dto.EventUpdateDto;
import samwells.io.calendar.dto.PaginatedEventDto;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.exception.InvalidCursorException;
import samwells.io.calendar.mapper.Mapper;
import samwells.io.calendar.service.EventService;
import samwells.io.calendar.util.StringUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
    private final EventService eventService;
    private final Mapper<Event, EventDto> eventMapper;
    private final StringUtil stringUtil;

    public EventsController(EventService eventService, Mapper<Event, EventDto> eventMapper, StringUtil stringUtil) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.stringUtil = stringUtil;
    }

    @PostMapping
    EventDto createEvent(@RequestBody CreateEventDto createEventDto) {
        Event newEvent = eventService.createEvent(
                createEventDto.title(),
                createEventDto.startTime(),
                createEventDto.duration(),
                createEventDto.durationUnit(),
                createEventDto.participants()
        );

        return new EventDto(newEvent);
    }

    @GetMapping
    PaginatedEventDto getEvents(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String lastStartTime,
            @RequestParam(required = false) Long lastId
    ) {
        validatePaginationParams(lastStartTime, lastId);

        List<EventDto> events = eventService
                .getEvents(startTime, endTime, lastStartTime, lastId)
                .stream()
                .map(eventMapper::map)
                .toList();

        return new PaginatedEventDto(events);
    }

    private void validatePaginationParams(String lastStartTime, Long lastId) {
        if (stringUtil.isNullOrEmpty(lastStartTime) && lastId != null) throw new InvalidCursorException("Both lastStartTime and lastId must be provided - Missing lastStartTime");
        if (!stringUtil.isNullOrEmpty(lastStartTime) && lastId == null) throw new InvalidCursorException("Both lastStartTime and lastId must be provided - Missing lastId");
    }

    @GetMapping("/{id}")
    EventDto getEvent(@PathVariable Long id) {
        return eventMapper.map(eventService.getEvent(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    EventDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventUpdateDto) {
        Event updatedEvent = eventService.updateEvent(
                id,
                eventUpdateDto.title(),
                eventUpdateDto.startTime(),
                eventUpdateDto.duration(),
                eventUpdateDto.durationUnit()
        );

        return eventMapper.map(updatedEvent);
    }
}
