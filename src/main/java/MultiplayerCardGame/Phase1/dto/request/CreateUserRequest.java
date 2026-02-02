package MultiplayerCardGame.Phase1.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 30)
    private String displayName;
}
