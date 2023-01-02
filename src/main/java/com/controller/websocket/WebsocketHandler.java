package com.controller.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.websocket.ChatMessage;
import com.model.websocket.ChatRoom;
import com.service.WebsocketService;
import com.util.Encryption.EncryptionService;
import com.util.Encryption.JWTEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class WebsocketHandler extends TextWebSocketHandler {
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private WebsocketService websocketService;
    @Autowired
    private ObjectMapper ObjectSocketMapper;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        try {
            log.info("WebsocketRoomHandler -> handleMessage -> {}", message.getPayload());
            Map<String, Object> httpSessionDatas = session.getAttributes();
            String email = encryptionService.getSessionParameter(httpSessionDatas.get(JWTEnum.JWTToken.name()).toString(), JWTEnum.EMAIL.name());
            ChatMessage chatMessage = ObjectSocketMapper.readValue(message.getPayload().toString(), ChatMessage.class);
            ChatRoom room = websocketService.findRoomById(chatMessage.getRoomId());
            if (room != null) {
                chatMessage.setSender(email);
                room.handleActions(session, chatMessage, websocketService);
            } else {
                ChatRoom createdRoom = websocketService.createRoom("Room Name Create");
                chatMessage.setRoomId(createdRoom.getRoomId());
                chatMessage.setType(ChatMessage.MessageType.ENTER);
                chatMessage.setSender(email);
                createdRoom.handleActions(session, chatMessage, websocketService);
            }
        } catch (JsonMappingException e) {
            session.close(CloseStatus.BAD_DATA);
        } catch (JsonProcessingException e) {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebsocketHandler -> afterConnectionEstablished : {}", session.getId());
        websocketService.getSocketQueue().add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebsocketHandler -> afterConnectionClosed : {}", session.getId());
        websocketService.getSocketQueue().remove(session);
        super.afterConnectionClosed(session, status);
    }
}
