package MultiplayerCardGame.Phase1.service;

import MultiplayerCardGame.Phase1.dto.response.RoomResponse;
import MultiplayerCardGame.Phase1.model.GameRoom;
import MultiplayerCardGame.Phase1.repository.GameStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameStore gameStroe;

    public RoomResponse startGame(String roomCode){
        return gameStroe.startGame(roomCode);
    }
}
