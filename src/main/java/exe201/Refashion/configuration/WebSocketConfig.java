package exe201.Refashion.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // client sẽ subscribe /topic/*
        config.enableSimpleBroker("/topic");
        // client sẽ send đến /app/*
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // client connect tới ws://host/ws-chat
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
    }
}

