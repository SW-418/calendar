package samwells.io.calendar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Entity
@Getter
@Setter
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_time", nullable = false)
    Instant startTime;

    @Column(name = "end_time", nullable = false)
    Instant endTime;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration_unit", nullable = false)
    ChronoUnit durationUnit;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    User owner;

    @ManyToMany
    @JoinTable(
            name = "event_participant",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    Set<User> participants;

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @Version
    Long version;
}
