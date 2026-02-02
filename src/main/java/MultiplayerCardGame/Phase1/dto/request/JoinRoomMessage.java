package MultiplayerCardGame.Phase1.dto.request;

import lombok.Data;

@Data
public class JoinRoomMessage {
    private String roomCode;
    private String userId;
    private String displayName;
}
