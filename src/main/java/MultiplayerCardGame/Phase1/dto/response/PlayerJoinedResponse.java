package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlayerJoinedResponse {
    private String type = "PLAYER_JOINED";
    private PlayerInfo player;
    private List<PlayerInfo> currentPlayers;

    @Data
    @Builder
    public static class PlayerInfo {
        private String userId;
        private String displayName;
        private boolean isHost;
    }
}
