package com.config;

import com.model.queue.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Server Queue 개선
 *
 * @Author kimwoosik
 * @Date 2022-07-26
 * @Description Email Token 및 각종 인증 토큰의 정보를 가지고 있는 Queue
 */
@Configuration
@Slf4j
public class QueueConfig {
    public QueueConfig() {
        log.info("Queue Initialized");
    }

    @Bean
    public synchronized HashMap<String, Token> TokenQueue() {
        log.info("SplitFileStorage Initialized");
        return new HashMap<>();
    }
}
