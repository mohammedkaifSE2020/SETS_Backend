package MultiplayerCardGame.Phase1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String type = "ERROR";
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
}
