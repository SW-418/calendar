package samwells.io.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samwells.io.calendar.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId")
    List<Event> getEventsForUser(@Param("userId") Long userId);

    @Query("SELECT e FROM Event e JOIN e.participants p WHERE e.id = :eventId AND p.id = :userId")
    Optional<Event> getEventForUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Event e WHERE e.id = :eventId AND e.owner.id = :userId")
    int deleteEventForUser(@Param("eventId") Long eventId, @Param("userId") Long userId);
}
