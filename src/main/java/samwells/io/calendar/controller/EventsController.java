package samwells.io.calendar.controller;

import org.springframework.web.bind.annotation.*;
import samwells.io.calendar.dto.CreateEventDto;
import samwells.io.calendar.dto.EventDto;
import samwells.io.calendar.entity.Event;
import samwells.io.calendar.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
    private final EventService eventService;

    public EventsController(EventService eventService) {
        this.eventService = eventService;
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
        return eventService.getEvents().stream().map(EventDto::new).toList();
    }
}
