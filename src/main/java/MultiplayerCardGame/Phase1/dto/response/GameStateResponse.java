package MultiplayerCardGame.Phase1.dto.response;

import MultiplayerCardGame.Phase1.model.GameStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameStateResponse {
    private String type = "STATE_SYNC";
    private List<String> yourCards;
    private String currentTurn;
    private List<PlayerState> playerOrder;
    private GameStatus gameStatus;

    @Data
    @Builder
    public static class PlayerState {
        private String userId;
        private String displayName;
        private int cardCount;
        private boolean isYourTurn;
    }
}
