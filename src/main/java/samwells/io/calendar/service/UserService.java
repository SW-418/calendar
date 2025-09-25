package samwells.io.calendar.service;

import org.springframework.security.provisioning.UserDetailsManager;
import samwells.io.calendar.entity.User;

import java.util.Set;

public interface UserService extends UserDetailsManager {
    Set<User> getUsersById(Set<Long> ids);
}
