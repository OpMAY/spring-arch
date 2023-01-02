package com.model.websocket;

import lombok.Data;

/**
 * {
 * "type": "ENTER",
 * "sender": "kimwoosik"
 * }
 * {
 * "type": "TALK",
 * "sender": "kimwoosik",
 * "roomId":"f212004c-0deb-42b2-8c4a-5a5d09d9b7bd",
 * "message":"message test"
 * }
 */
@Data
public class ChatMessage {
    public enum MessageType {
        ENTER, OUT, TALK, ERROR
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}