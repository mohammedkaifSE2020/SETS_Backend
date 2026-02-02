package MultiplayerCardGame.Phase1.service;

import MultiplayerCardGame.Phase1.dto.response.RoomResponse;
import MultiplayerCardGame.Phase1.repository.GameStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final GameStore gameStore;

    public RoomResponse getRoom(String roomCode){
        return gameStore.getRoomResponseByCode(roomCode);
    }
}
