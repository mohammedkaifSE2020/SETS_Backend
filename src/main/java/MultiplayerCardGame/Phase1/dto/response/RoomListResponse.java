package MultiplayerCardGame.Phase1.dto.response;

import MultiplayerCardGame.Phase1.model.GameStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomListResponse {
    private List<RoomSummary> rooms;

    @Data
    @Builder
    public static class RoomSummary {
        private String roomCode;
        private String hostName;
        private int currentPlayerCount;
        private int maxPlayers;
        private GameStatus status;
        private boolean isPrivate;
    }

    //public static RoomListResponse fromGameRooms(Collection<GameRoom> rooms);
}
