package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.websocket.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Configuration
@Slf4j
public class SocketQueueConfig {
    public SocketQueueConfig() {
        log.info("SocketQueueConfig Initialized");
    }

    @Bean
    public synchronized List<WebSocketSession> SocketQueue() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    @Bean
    public synchronized ObjectMapper ObjectSocketMapper() {
        return new ObjectMapper();
    }

    @Bean
    public synchronized HashMap<String, ChatRoom> SocketRoomQueue() {
        return new HashMap<String, ChatRoom>();
    }
}
