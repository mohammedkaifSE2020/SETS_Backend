package MultiplayerCardGame.Phase1.repository;

import MultiplayerCardGame.Phase1.dto.request.CreateRoomRequest;
import MultiplayerCardGame.Phase1.dto.request.CreateUserRequest;
import MultiplayerCardGame.Phase1.dto.response.RoomResponse;
import MultiplayerCardGame.Phase1.dto.response.UserResponse;
import MultiplayerCardGame.Phase1.model.GameRoom;
import MultiplayerCardGame.Phase1.model.GameStatus;
import MultiplayerCardGame.Phase1.model.Player;
import MultiplayerCardGame.Phase1.model.User;
import MultiplayerCardGame.Phase1.model.CardTheme;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.serial.SerialStruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Repository
public class GameStore {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> usernameIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> roomCodeIndex = new ConcurrentHashMap<>();

    public UserResponse addUser(String userId, User user) {
        // 1️⃣ Validate input
        if (user == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            throw new IllegalArgumentException("Display name is required");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        // 2️⃣ Check for duplicate username
        if (usernameIndex.containsKey(user.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }


        // 5️⃣ Store user
        users.put(userId, user);
        usernameIndex.put(user.getUsername(), userId);

        // 6️⃣ Return response DTO
        UserResponse response = UserResponse.builder()
                .userId(user.getUserId())
                .displayName(user.getDisplayName())
                .username(user.getUsername())
                .gamesPlayed(user.getGamesPlayed())
                .gamesWon(user.getGamesWon())
                .createdAt(user.getCreatedAt())
                .build();

        return response;
    }

    public UserResponse getUser(String userId) {
        User user = users.get(userId);

        if (user == null) {
            return null;
        }
        UserResponse response = UserResponse.builder()
                .userId(user.getUserId())
                .displayName(user.getDisplayName())
                .username(user.getUsername())
                .gamesPlayed(user.getGamesPlayed())
                .gamesWon(user.getGamesWon())
                .createdAt(user.getCreatedAt())
                .build();
        return response;
    }

    public UserResponse getUserByUserName(String userName) {
        //get the id by userName Index hashmap
        String id = usernameIndex.get(userName.toLowerCase());

        if (id != null) {
            User user = users.get(id);

            if (user == null) {
                return null;
            }
            UserResponse response = UserResponse.builder()
                    .userId(user.getUserId())
                    .displayName(user.getDisplayName())
                    .username(user.getUsername())
                    .gamesPlayed(user.getGamesPlayed())
                    .gamesWon(user.getGamesWon())
                    .createdAt(user.getCreatedAt())
                    .build();
            return response;
        }
        return null;
    }

    public boolean userExiste(String userName) {
        return (usernameIndex.containsKey(userName.toLowerCase()));
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        User host = users.get(request.getHostUserId());
        if (host == null) throw new IllegalArgumentException("Host user not found");

        String roomId = UUID.randomUUID().toString();
        String roomCode = generateRoomCode();

        // 1. Create Internal Player
        Player hostPlayer = Player.builder()
                .userId(host.getUserId())
                .displayName(host.getDisplayName())
                .cards(new ArrayList<>())
                .isHost(true)
                .isConnected(true)
                .isReady(true)
                .joinedAt(LocalDateTime.now())
                .build();

        // 2. Build Internal Room
        GameRoom newRoom = GameRoom.builder()
                .roomId(roomId)
                .roomCode(roomCode)
                .hostUserId(host.getUserId())
                .hostName(host.getDisplayName())
                .players(new ArrayList<>(List.of(hostPlayer)))
                .status(GameStatus.WAITING)
                .maxPlayers(request.getMaxPlayers())
                .isPrivate(request.isPrivate())
                .createdAt(LocalDateTime.now())
                .build();

        // 3. Store in memory
        rooms.put(roomId, newRoom);
        roomCodeIndex.put(roomCode, roomId);

        // 4. Use a helper to return the response (Keeps this method clean!)
        return convertToResponse(newRoom);
    }

    // This helper will be VERY useful for the "Join Room" logic later!
    private RoomResponse convertToResponse(GameRoom room) {
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomCode(room.getRoomCode())
                .hostUserId(room.getHostUserId())
                .hostName(room.getHostName())
                .status(room.getStatus())
                .maxPlayers(room.getMaxPlayers())
                .currentPlayerCount(room.getPlayers().size())
                .isPrivate(room.isPrivate())
                .createdAt(room.getCreatedAt())
                .players(
                        room.getPlayers().stream()
                                .map(player -> RoomResponse.PlayerInfo.builder()
                                        .userId(player.getUserId())
                                        .displayName(player.getDisplayName())
                                        .isHost(player.isHost())
                                        .isReady(player.isReady())
                                        .cards(player.getCards()) // ✅ THIS WAS MISSING
                                        .build()
                                )
                                .toList()
                )
                .build();
    }


    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public RoomResponse joinRoom(String roomCode, String userId) {
        // 1. Find the room ID using your index
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());
        if (roomId == null) throw new RuntimeException("Room not found");

        GameRoom room = rooms.get(roomId);
        User user = users.get(userId);

        // 2. Validation
        if (room.getPlayers().size() >= room.getMaxPlayers()) {
            throw new RuntimeException("Room is full");
        }

        // Check if user is already in the room
        boolean alreadyIn = room.getPlayers().stream()
                .anyMatch(p -> p.getUserId().equals(userId));

        if (!alreadyIn) {
            Player newPlayer = Player.builder()
                    .userId(user.getUserId())
                    .displayName(user.getDisplayName())
                    .isHost(false)
                    .isReady(false)
                    .joinedAt(LocalDateTime.now())
                    .cards(new ArrayList<>())
                    .build();
            room.getPlayers().add(newPlayer);
        }

        return convertToResponse(room);
    }

    public RoomResponse getRoomResponseByCode(String roomCode) {
        // 1. Find the UUID using the index (handle case sensitivity just in case)
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());

        if (roomId == null) {
            // You should handle this with a custom exception or a 404 in the controller
            throw new RuntimeException("Room not found with code: " + roomCode);
        }

        // 2. Retrieve the actual room object
        GameRoom room = rooms.get(roomId);

        if (room == null) {
            throw new RuntimeException("Room data inconsistency for code: " + roomCode);
        }

        // 3. Convert the internal Model to the Response DTO
        return convertToResponse(room);
    }

    public RoomResponse startGame(String roomCode) {
        String roomId = roomCodeIndex.get(roomCode);
        GameRoom room = rooms.get(roomId);

        // 1. Prepare the Deck
        List<String> deck = new ArrayList<>();
        int numPlayers = room.getPlayers().size();

        // Pick as many sets as there are players
        for (int i = 0; i < numPlayers; i++) {
            String animal = CardTheme.ANIMALS.get(i);
            for (int j = 0; j < 4; j++) {
                deck.add(animal); // Add 4 of each animal
            }
        }

        // 2. Shuffle the Deck
        Collections.shuffle(deck);

        // 3. Distribute Cards
        for (int i = 0; i < numPlayers; i++) {
            // Give each player 4 cards from the deck
            List<String> hand = new ArrayList<>(deck.subList(i * 4, (i + 1) * 4));
            room.getPlayers().get(i).setCards(hand);
        }

        // 4. Update Room Status
        room.setStatus(GameStatus.PLAYING);
        room.setCurrentTurnIndex(0); // Player 1 starts


        return convertToResponse(room);
    }

    public boolean isHost(String roomCode, String userId) {
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());
        if (roomId == null) return false;

        GameRoom room = rooms.get(roomId);
        // Just compare the IDs directly!
        return room != null && room.getHostUserId().equals(userId);
    }

    //card pasing logic
    public RoomResponse passCard(String roomCode, String fromUserId, String cardValue) {
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());
        GameRoom room = rooms.get(roomId);
        List<Player> players = room.getPlayers();

        // 1. Find the index of the person passing the card
        int senderIdx = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUserId().equals(fromUserId)) {
                senderIdx = i;
                break;
            }
        }


        if (senderIdx == -1) throw new RuntimeException("Player not found");

        // 2. Calculate the neighbor (The "Next" person)
        int receiverIdx = (senderIdx + 1) % players.size();

        Player sender = players.get(senderIdx);
        Player receiver = players.get(receiverIdx);

        if (sender.getCards().size() < 4) {
            throw new RuntimeException("Wait for a card before passing again!");
        }

        // 3. Perform the handoff
        // remove returns true if the card existed in the sender's hand
        if (sender.getCards().remove(cardValue)) {
            receiver.getCards().add(cardValue);
            System.out.println("Passed " + cardValue + " from " + sender.getDisplayName() + " to " + receiver.getDisplayName());
        }

        return convertToRoomResponseFromRoomWithGameAction(room, sender, receiver);
    }

    public RoomResponse convertToRoomResponseFromRoomWithGameAction(GameRoom room, Player sender, Player receiver) {
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomCode(room.getRoomCode())
                .hostUserId(room.getHostUserId())
                .hostName(room.getHostName())
                .status(room.getStatus())
                .maxPlayers(room.getMaxPlayers())
                .currentPlayerCount(room.getPlayers().size())
                .isPrivate(room.isPrivate())
                .createdAt(room.getCreatedAt())
                .players(
                        room.getPlayers().stream()
                                .map(player -> RoomResponse.PlayerInfo.builder()
                                        .userId(player.getUserId())
                                        .displayName(player.getDisplayName())
                                        .isHost(player.isHost())
                                        .isReady(player.isReady())
                                        .cards(player.getCards())
                                        .build()
                                )
                                .toList()
                )
                .lastAction(RoomResponse.GameAction.builder()
                        .type(room.getStatus() == GameStatus.FINISHED ? "WIN" : "PASS")
                        .senderId(sender.getUserId())
                        .senderName(sender.getDisplayName())
                        .receiverId(receiver != null ? receiver.getUserId() : null)
                        .receiverName(receiver != null ? receiver.getDisplayName() : null)
                        .build())
                .build();

    }

    public RoomResponse declareWinner(String roomCode, String userId) {
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());
        GameRoom room = rooms.get(roomId);

        // 1. Find the player making the claim
        Player claimant = room.getPlayers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // 2. Server-side Validation: Does this player actually have 4 of a kind?
        List<String> cards = claimant.getCards();
        boolean isValidSet = cards.size() == 4 && cards.stream().allMatch(c -> c.equals(cards.get(0)));

        if (isValidSet) {
            // 3. Update Room State
            room.setStatus(GameStatus.FINISHED);
            room.setWinner(claimant.getDisplayName()); // Assuming you have a winner field in GameRoom
        } else {
            // Optional: Penalize for a false claim?
            // For now, we just log it or throw an error.
            throw new RuntimeException("Invalid set declaration by user: " + claimant.getDisplayName());
        }

        // Reuse your conversion logic (passing null for receiver since it's a win, not a pass)
        return convertToRoomResponseFromRoomWithGameAction(room, claimant, null);
    }

    public String processWin(String roomCode, String userId) {
        // 1. Retrieve the room
        String roomId = roomCodeIndex.get(roomCode.toUpperCase());
        GameRoom room = rooms.get(roomId);

        if (room == null) throw new RuntimeException("Room not found");

        // 2. Find the player who claimed the win
        Player winner = room.getPlayers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Winner not found in room"));

        // 3. Update Room Status to prevent further card passing
        // Using FINISHED or a similar status from your GameStatus enum
        room.setStatus(GameStatus.FINISHED);

        // 4. Record the winner name in the room model for persistence
        room.setWinner(winner.getDisplayName());

        // 5. Return the name to be used in the GameWonResponse DTO
        return winner.getDisplayName();
    }
}
