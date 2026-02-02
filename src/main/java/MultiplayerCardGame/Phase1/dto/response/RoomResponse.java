package MultiplayerCardGame.Phase1.dto.response;

import MultiplayerCardGame.Phase1.model.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RoomResponse {
    private String roomId;
    private String roomCode;
    private String hostUserId;
    private String hostName;
    private List<PlayerInfo> players;
    private List<String> deck;
    private GameStatus status;
    private int currentPlayerCount;
    private int maxPlayers;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private GameAction lastAction;

    @Data
    @Builder
    public static class GameAction {
        private String type; // "PASS" or "WIN"
        private String senderId;
        private String senderName;
        private String receiverId;
        private String receiverName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerInfo {
        private String userId;
        private String displayName;
        private Boolean isHost;
        private Boolean isReady;
        private List<String> cards;
    }


}
