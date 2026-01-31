package com.cananalyzer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.cananalyzer.controller.CANWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CANWebSocketHandler canWebSocketHandler;

    public WebSocketConfig(CANWebSocketHandler canWebSocketHandler) {
        this.canWebSocketHandler = canWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(canWebSocketHandler, "/ws/can").setAllowedOrigins("*");
    }
}
