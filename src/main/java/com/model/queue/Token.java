package com.model.queue;

import com.model.common.Time;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * PR
 *
 * @Date 2022-07-27
 * @Author kimwoosik
 * @Description Token Extends TokenTime in Builder Pattern
 * Email Auth (REGISTER), Password Find Auth (Password find, FIND), WithDrawal(SignOut Forever,WITHDRAWAL)
 */

@Getter
@ToString(callSuper = true)
public class Token extends TokenTime {
    private String email;
    private String token;
    private ServerTokenType type;

    @Builder
    public Token(LocalDateTime reg_datetime, LocalDateTime update_datetime, String email, String token, ServerTokenType type) {
        super(reg_datetime, update_datetime);
        this.email = email;
        this.token = token;
        this.type = type;
    }
}
