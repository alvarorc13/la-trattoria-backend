package com.latrattoria.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @org.springframework.beans.factory.annotation.Autowired
    private com.latrattoria.backend.security.JwtUtil jwtUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200", "https://la-trattoria-frontend.vercel.app") // añadir localhost para dev
                .setHandshakeHandler(new com.latrattoria.backend.config.CustomHandshakeHandler())
                .addInterceptors(new com.latrattoria.backend.security.JwtHandshakeInterceptor(jwtUtil))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // canal de salida hacia clientes
        registry.setApplicationDestinationPrefixes("/app"); // prefijo si el cliente envía mensajes al servidor
    }
}