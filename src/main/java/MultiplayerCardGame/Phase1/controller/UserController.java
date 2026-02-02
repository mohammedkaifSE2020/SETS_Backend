package MultiplayerCardGame.Phase1.controller;

import MultiplayerCardGame.Phase1.dto.request.CreateUserRequest;
import MultiplayerCardGame.Phase1.dto.response.UserResponse;
import MultiplayerCardGame.Phase1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        log.info("Received user registration request for username: {}", request.getUsername());
        try {
            UserResponse response = userService.registerUser(request);
            log.info("User registered successfully with userId: {}", response.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            log.error("Error registering user with username: {}", request.getUsername(), ex);
            throw ex; // Let @ControllerAdvice handle it
        }
    }

}
