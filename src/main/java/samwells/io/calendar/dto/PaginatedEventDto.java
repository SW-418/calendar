package samwells.io.calendar.dto;

import java.util.List;

public record PaginatedEventDto(
        List<EventDto> data,
        String lastStartTime,
        Long lastId
) {
    public PaginatedEventDto(List<EventDto> data) {
        this(
                data,
                data.isEmpty() ? null : data.getLast().startTime(),
                data.isEmpty() ? null : data.getLast().id()
        );
    }
}
