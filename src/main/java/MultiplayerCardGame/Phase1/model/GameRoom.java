package MultiplayerCardGame.Phase1.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRoom {
    private String roomId;
    private String roomCode;
    private String hostUserId;
    private String hostName;
    private List<Player> players;
    private List<String> deck;
    private int currentTurnIndex;
    private GameStatus status;
    private String winner;
    private int maxPlayers;
    private boolean isPrivate;
    private LocalDateTime createdAt;

}
