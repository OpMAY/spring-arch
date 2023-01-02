package com.config;

import com.controller.websocket.WebsocketHandler;
import com.interceptor.websocket.SessionHandShakeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Slf4j
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    /***
     *     WebSocketHandler를 추가한다.
     *     1. 클라이언트가 접속을 했을 때 특정 메소드가 호출
     *     2. 클라이언트가 접속을 close했을 때 특정 메소드가 호출
     *     3. 클라이언트가 메시지를 보냈을 때 특정 메소드 호출
     * */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/websocket")
                .setAllowedOrigins("*")
                //.setHandshakeHandler(new SessionHandShakeHandler()) 구현체 필요 (테스트 버전에선 미요구)
                .addInterceptors(new SessionHandShakeInterceptor());
        log.info("WebsocketConfig Handler Registered");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebsocketHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        // WebSocket의 런타임 특성 제어
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
