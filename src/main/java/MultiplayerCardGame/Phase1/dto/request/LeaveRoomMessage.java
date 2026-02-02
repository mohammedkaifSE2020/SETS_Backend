package MultiplayerCardGame.Phase1.dto.request;

import lombok.Data;

@Data
public class LeaveRoomMessage {
    private String roomCode;
    private String userId;
}
