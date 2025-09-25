package samwells.io.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samwells.io.calendar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
