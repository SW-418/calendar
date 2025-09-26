package samwells.io.calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samwells.io.calendar.dto.CreateEventDto;
import samwells.io.calendar.dto.EventDto;
import samwells.io.calendar.dto.EventUpdateDto;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.mapper.Mapper;
import samwells.io.calendar.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
    private final EventService eventService;
    private final Mapper<Event, EventDto> eventMapper;

    public EventsController(EventService eventService, Mapper<Event, EventDto> eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
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
    List<EventDto> getEvents() {
        return eventService.getEvents().stream().map(eventMapper::map).toList();
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
