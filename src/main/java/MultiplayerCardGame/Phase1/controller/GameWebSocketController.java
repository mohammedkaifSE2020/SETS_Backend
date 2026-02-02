package MultiplayerCardGame.Phase1.controller;

import MultiplayerCardGame.Phase1.dto.request.CreateRoomRequest;
import MultiplayerCardGame.Phase1.dto.request.JoinRoomMessage;
import MultiplayerCardGame.Phase1.dto.request.PassCardMessage;
import MultiplayerCardGame.Phase1.dto.response.GameWonResponse;
import MultiplayerCardGame.Phase1.dto.response.RoomResponse;
import MultiplayerCardGame.Phase1.model.GameRoom;
import MultiplayerCardGame.Phase1.repository.GameStore;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameStore gameStore;

    @MessageMapping("/create-room")
    public void createRoom(CreateRoomRequest roomRequest){
        RoomResponse newRoom = gameStore.createRoom(roomRequest);

        messagingTemplate.convertAndSend("/topic/rooms", newRoom);
    }

    @MessageMapping("/join-room/{roomCode}")
    public void joinRoom(@DestinationVariable String roomCode, JoinRoomMessage request) {
        RoomResponse updatedRoom = gameStore.joinRoom(roomCode, request.getUserId());

        // Broadcast to the specific room topic
        // This ensures Player 1 (the host) sees Player 2's name pop up immediately!
        messagingTemplate.convertAndSend("/topic/room/" + roomCode, updatedRoom);

        // Also update the general lobby so others see the player count increase
        messagingTemplate.convertAndSend("/topic/rooms", updatedRoom);
    }

    @MessageMapping("/start-game/{roomCode}")
    public void handleStartGame(@DestinationVariable String roomCode, Map<String, String> payload) {
        String userId = payload.get("userId");

        // Security Check: Is this person actually the host?
        if (gameStore.isHost(roomCode, userId)) {
            RoomResponse startedGame = gameStore.startGame(roomCode);

            // Notify everyone in the room!
            messagingTemplate.convertAndSend("/topic/room/" + roomCode, startedGame);
        }
    }

    @MessageMapping("/pass-card/{roomCode}")
    public void handlePassCard(@DestinationVariable String roomCode, PassCardMessage request) {
        // Process the move
        RoomResponse updatedRoom = gameStore.passCard(roomCode, request.getUserId(), request.getCardValue());

        // Broadcast the updated state to everyone in the room
        messagingTemplate.convertAndSend("/topic/room/" + roomCode, updatedRoom);
    }

    @MessageMapping("/declare-set/{roomCode}")
    public void handleDeclareSet(@DestinationVariable String roomCode, Map<String, String> payload) {
        String userId = payload.get("userId");

        // 1. Logic to set room status and get winner name
        String winnerName = gameStore.processWin(roomCode, userId);

        // 2. Create the simple response
        GameWonResponse winResponse = GameWonResponse.builder()
                .type("GAME_WON")
                .winner(winnerName)
                .build();

        // 3. Broadcast to all players in that room
        messagingTemplate.convertAndSend("/topic/room/" + roomCode, winResponse);
    }

}
