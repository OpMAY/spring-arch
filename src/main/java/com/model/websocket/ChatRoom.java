package com.model.websocket;

import com.service.WebsocketService;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ChatRoom {
    private String roomId;
    private String name;
    private List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, WebsocketService websocketService) {
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            this.sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셧습니다.");
            sendMessage(chatMessage, websocketService);
        } else {
            sendMessage(chatMessage, websocketService);
        }
    }

    public <T> void sendMessage(T message, WebsocketService websocketService) {
        synchronizedWebsocketSessions(websocketService);
        this.sessions.parallelStream().forEach(session -> {
            try {
                if (session.isOpen()) {
                    websocketService.sendMessage(session, message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void synchronizedWebsocketSessions(WebsocketService websocketService) {
        this.sessions.parallelStream().forEach(session -> {
            if (!session.isOpen()) {
                this.sessions.remove(session);
            }
        });
    }
}