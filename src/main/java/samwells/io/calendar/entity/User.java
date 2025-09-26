package samwells.io.calendar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import samwells.io.calendar.exception.InvalidTimezonePreferenceException;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(name = "first_name", nullable = false)
    String firstname;

    @Column(name = "surname", nullable = false)
    String surname;

    @Column(name = "timezone_preference")
    String timezonePreference;

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @Version
    Long version;

    public User() {}
    public User(String email, String password, String firstname, String surname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
    }

    public User(String email, String password, String firstname, String surname, String timezonePreference) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
        setTimezonePreference(timezonePreference);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setTimezonePreference(String timezonePreference) {
        try {
            this.timezonePreference = ZoneId.of(timezonePreference).getId();
        } catch (DateTimeException exception) {
            throw new InvalidTimezonePreferenceException(timezonePreference);
        }
    }
}
