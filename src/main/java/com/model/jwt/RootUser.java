package com.model.jwt;

import com.model.common.Time;
import lombok.Data;

@Data
abstract public class RootUser extends Time {
    private String version;
    private String grant;
    private String access_token;
    private String email;
    private String id;
    private int no;
}