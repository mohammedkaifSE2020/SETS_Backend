package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String username;
    private String displayName;
    private int gamesPlayed;
    private int gamesWon;
    private LocalDateTime createdAt;

    //public static UserResponse fromUser(User user);
}
