package MultiplayerCardGame.Phase1.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "Host user ID is required")
    private String hostUserId;

    @Min(3)
    @Max(5)
    private int maxPlayers = 8;

    private boolean isPrivate = true;
}