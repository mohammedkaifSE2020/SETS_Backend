package MultiplayerCardGame.Phase1.dto.request;

import lombok.Data;

@Data
public class StartGameMessage {
    private String roomCode;
    private String userId;
}
