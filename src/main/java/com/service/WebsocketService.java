package com.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.websocket.ChatRoom;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class WebsocketService {
    //모든 웹소켓이 들어있는 Queue
    private final List<WebSocketSession> SocketQueue;
    //룸 목록이 들어있는 Queue
    private final HashMap<String, ChatRoom> SocketRoomQueue;
    //Chat Message Change Helper
    private final ObjectMapper ObjectSocketMapper;

    @PostConstruct
    private void init() {
        //Initialize WebsocketService
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(SocketRoomQueue.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return SocketRoomQueue.get(roomId);
    }

    public ChatRoom createRoom(String roomId, String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
        SocketRoomQueue.put(roomId, chatRoom);
        return chatRoom;
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        SocketRoomQueue.put(randomId, chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) throws IOException {
        session.sendMessage(new TextMessage(ObjectSocketMapper.writeValueAsString(message)));
    }
}
