package MultiplayerCardGame.Phase1.service;

import MultiplayerCardGame.Phase1.dto.request.CreateUserRequest;
import MultiplayerCardGame.Phase1.dto.response.UserResponse;
import MultiplayerCardGame.Phase1.exception.UserNameAlreadyExists;
import MultiplayerCardGame.Phase1.model.User;
import MultiplayerCardGame.Phase1.repository.GameStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GameStore gameStore;

    public UserResponse registerUser(CreateUserRequest request) {

        String userName = request.getUsername();

        if (gameStore.userExiste(userName)) {
            throw new UserNameAlreadyExists("Username '{username}' is already taken");
        }

        String userId = UUID.randomUUID().toString();

        User user = User.builder()
                .userId(userId)
                .email(request.getEmail())
                .username(request.getUsername())
                .displayName(request.getDisplayName())
                .gamesPlayed(0)
                .gamesWon(0)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponse response = gameStore.addUser(userId, user);

        return response;
    }


}
