package MultiplayerCardGame.Phase1.model;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    private String sessionId;
    private String userId;
    private String displayName;
    private List<String> cards;
    private boolean isHost;
    private boolean isConnected;
    private boolean isReady;
    private LocalDateTime joinedAt;



    public boolean hasCard(String cardName) {
        if (cards == null || cardName == null) return false;

        for (String card : cards) {
            if (cardName.equalsIgnoreCase(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeCard(String cardName){
        if (cards == null || cardName == null) return false;

        for(String card: cards){
            if(cardName.equalsIgnoreCase(card)){
                cards.remove(card);
                return true;
            }
        }
        return false;
    }

    public boolean addCard(String cardName){
        return cards.add(cardName);
    }
}


