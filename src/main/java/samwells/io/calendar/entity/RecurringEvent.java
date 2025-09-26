package samwells.io.calendar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "recurring_event")
public class RecurringEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(name = "start_time", nullable = false)
    Instant startTime;

    @Column(name = "end_time")
    Instant endTime;

    @Column(nullable = false)
    int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration_unit", nullable = false)
    ChronoUnit durationUnit;

    @Column(nullable = false)
    int repetition;

    @Enumerated(EnumType.STRING)
    @Column(name = "repetition_unit", nullable = false)
    ChronoUnit repetitionUnit;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    User owner;

    @ManyToMany
    @JoinTable(
            name = "recurring_event_participant",
            joinColumns = @JoinColumn(name = "recurring_event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    Set<User> participants = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @Version
    Long version;

    public RecurringEvent() { }

    public RecurringEvent(
            String title,
            Instant startTime,
            Instant endTime,
            int duration,
            ChronoUnit durationUnit,
            int repetition,
            ChronoUnit repetitionUnit,
            User owner
    ) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.owner = owner;
    }

    public RecurringEvent(
            String title,
            Instant startTime,
            int duration,
            ChronoUnit durationUnit,
            int repetition,
            ChronoUnit repetitionUnit,
            User owner
    ) {
        this.title = title;
        this.startTime = startTime;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.owner = owner;
    }
}
