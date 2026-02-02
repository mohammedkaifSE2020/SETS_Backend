package MultiplayerCardGame.Phase1.dto.request;

import lombok.Data;

@Data
public class PassCardMessage {
    private String userId;
    private String cardValue;
}
