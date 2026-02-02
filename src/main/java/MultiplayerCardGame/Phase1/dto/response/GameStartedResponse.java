package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameStartedResponse {
    private String type = "GAME_STARTED";
    private List<String> yourCards;
    private List<PlayerOrder> playerOrder;
    private String currentTurn;
    private int turnTimeLimit;

    @Data
    @Builder
    public static class PlayerOrder {
        private String userId;
        private String displayName;
        private int cardCount;
    }
}
