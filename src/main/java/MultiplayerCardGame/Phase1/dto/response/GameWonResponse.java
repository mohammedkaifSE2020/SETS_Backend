package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GameWonResponse {
    private String type = "GAME_WON";
    private String winner;
}
