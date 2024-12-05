package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * Конфигурационный класс для настройки WebSocket с использованием STOMP.
 * <p>
 * Реализует {@link WebSocketMessageBrokerConfigurer} для регистрации конечных точек.
 *
 * @author VladimirBoss
 * @see WebSocketMessageBrokerConfigurer
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * Регистрирует конечные точки STOMP для WebSocket.
     *
     * @param registry Регистратор конечных точек STOMP.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // Укажите конечную точку
    }
}