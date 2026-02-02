package MultiplayerCardGame.Phase1.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userId;
    private String username;
    private String email;
    private String displayName;
    private int gamesPlayed;
    private int gamesWon;
    private LocalDateTime createdAt;
}