package samwells.io.calendar.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samwells.io.calendar.entity.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("""
        SELECT e FROM Event e 
        JOIN e.participants p 
        WHERE p.id = :userId 
        AND (:startTime IS NULL OR e.startTime >= :startTime) 
        AND (:endTime IS NULL OR e.endTime <= :endTime) 
        AND (
            (:lastStartTime IS NULL AND :lastId IS NULL) OR 
            ((e.startTime, e.id) > (:lastStartTime, :lastId))
        )   
        ORDER BY e.startTime ASC, e.id ASC
    """)
    List<Event> getEventsForUser(
            @Param("userId") Long userId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("lastStartTime") Instant lastStartTime,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e JOIN e.participants p WHERE e.id = :eventId AND p.id = :userId")
    Optional<Event> getEventForUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Event e WHERE e.id = :eventId AND e.owner.id = :userId")
    int deleteEventForUser(@Param("eventId") Long eventId, @Param("userId") Long userId);
}
