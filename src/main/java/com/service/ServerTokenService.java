package com.service;

import com.model.queue.Token;
import com.util.Constant;
import com.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * PR
 *
 * @Date 2022-07-27
 * @Author kimwoosik
 * @Description Server Token Queue Service
 */

@Service
@Slf4j
public class ServerTokenService {

    @Autowired
    private HashMap<String, Token> TokenQueue;

    /**
     * token = serverTokenService.put(token, Token.builder()
     *                 .email("zlzldntlr@naver.com")
     *                 .token(token)
     *                 .type(ServerTokenType.REGISTER)
     *                 .reg_datetime(LocalDateTime.now())
     *                 .update_datetime(LocalDateTime.now())
     *                 .build());
     * */
    public String put(String key, Token buildToken) {
        if (exist(key)) {
            key = Utility.RandomToken(8);
        }
        TokenQueue.put(key, buildToken);
        return key;
    }

    public String toString() {
        return this.TokenQueue.toString();
    }

    public Token pop(String key) {
        if (exist(key)) {
            return this.TokenQueue.get(key);
        }
        return null;
    }

    public void remove(String key) {
        if (exist(key)) {
            this.TokenQueue.remove(key);
        }
    }

    public boolean exist(String key) {
        return this.TokenQueue.get(key) != null ? true : false;
    }

    /**
     * serverTokenService.removeOldKeys(serverTokenService.getOldKeys());
     * */
    public ArrayList<String> getOldKeys() {
        ArrayList<String> keys = new ArrayList<>();
        this.TokenQueue.forEach((key, value) -> {
            int result = value.getReg_datetime().truncatedTo(ChronoUnit.MINUTES).compareTo(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            log.info("getOldKeys Compare Result -> {}", result);
            if (Math.abs(result) > Constant.TOKEN_END_MINUTES) {
                keys.add(key);
            }
        });
        return keys;
    }

    /**
     * serverTokenService.removeOldKeys(serverTokenService.getOldKeys());
     * */
    public void removeOldKeys(ArrayList<String> oldKeys) {
        oldKeys.forEach((oldKey) -> {
            remove(oldKey);
        });
    }
}
