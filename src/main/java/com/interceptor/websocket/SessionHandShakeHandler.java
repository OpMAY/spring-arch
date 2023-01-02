package com.interceptor.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.Lifecycle;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;

import java.util.Map;

@Slf4j
@Component
public class SessionHandShakeHandler implements Lifecycle, HandshakeHandler {
    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        log.info("SessionHandShakeHandler -> doHandshake");
        return true;
    }

    @Override
    public void start() {
        log.info("SessionHandShakeHandler -> start");
    }

    @Override
    public void stop() {
        log.info("SessionHandShakeHandler -> stop");
    }

    @Override
    public boolean isRunning() {
        log.info("SessionHandShakeHandler -> isRunning");
        return true;
    }
}
