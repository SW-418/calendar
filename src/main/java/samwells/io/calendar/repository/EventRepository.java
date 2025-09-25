package samwells.io.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samwells.io.calendar.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId")
    List<Event> getEventsForUser(@Param("userId") Long userId);
}
