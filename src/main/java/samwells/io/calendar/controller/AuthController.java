package samwells.io.calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samwells.io.calendar.dto.SignupDto;
import samwells.io.calendar.entity.User;
import samwells.io.calendar.service.UserServiceImpl;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final UserServiceImpl userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupDto signupDto) {
        String timezonePreference = signupDto.timezonePreference();

        User user = new User(signupDto.email(), signupDto.password(), signupDto.firstname(), signupDto.surname());
        if (timezonePreference != null) user.setTimezonePreference(timezonePreference);

        userService.createUser(user);

        return ResponseEntity.ok().build();
    }
}
