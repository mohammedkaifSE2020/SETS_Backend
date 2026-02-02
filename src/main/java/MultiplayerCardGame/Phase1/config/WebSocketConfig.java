package MultiplayerCardGame.Phase1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Rooms/Events the client can SUBSCRIBE to (listen)
        config.enableSimpleBroker("/topic");

        // Prefix for messages sent FROM client TO server (@MessageMapping)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The URL the frontend connects to
        registry.addEndpoint("/ws-game")
                .setAllowedOrigins("http://localhost:5173") // Your React Port
                .withSockJS(); // Fallback for older browsers
    }
}
