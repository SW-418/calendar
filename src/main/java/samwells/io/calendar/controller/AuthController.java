package samwells.io.calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samwells.io.calendar.dto.SignupDto;
import samwells.io.calendar.entity.User;
import samwells.io.calendar.service.UserService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupDto signupDto) {
        userService.createUser(new User(signupDto.email(), signupDto.password(), signupDto.firstname(), signupDto.surname()));

        return ResponseEntity.ok().build();
    }
}
