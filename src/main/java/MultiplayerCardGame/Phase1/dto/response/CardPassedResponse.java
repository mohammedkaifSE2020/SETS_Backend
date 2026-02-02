package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardPassedResponse {
    private String type = "CARD_PASSED";
    private String fromPlayer;
    private String toPlayer;
    private String currentTurn;
}
