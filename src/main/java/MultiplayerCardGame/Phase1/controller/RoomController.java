package MultiplayerCardGame.Phase1.controller;

import MultiplayerCardGame.Phase1.dto.response.RoomResponse;
import MultiplayerCardGame.Phase1.model.GameRoom;
import MultiplayerCardGame.Phase1.service.GameService;
import MultiplayerCardGame.Phase1.service.RoomService;
import MultiplayerCardGame.Phase1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final GameService gameService;

    @GetMapping("/{roomCode}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String roomCode) {
        return ResponseEntity.ok(roomService.getRoom(roomCode));
    }

    @GetMapping("/game/{roomCode}")
    public ResponseEntity<RoomResponse> startGame(@PathVariable String roomCode){
        return ResponseEntity.ok(gameService.startGame(roomCode));
    }
}