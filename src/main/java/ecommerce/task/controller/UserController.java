package ecommerce.task.controller;

import ecommerce.task.dto.UpdateProfileRequest;
import ecommerce.task.model.User;
import ecommerce.task.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(
            Authentication authentication) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        User user =
                userRepository.findByEmail(
                        authentication.getName()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        user.setName(
                request.getName().trim()
        );

        return ResponseEntity.ok(
                userRepository.save(user)
        );
    }
}