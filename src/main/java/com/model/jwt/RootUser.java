package com.model.jwt;

import com.model.common.Time;
import com.model.grant.GrantType;
import lombok.Data;

@Data
abstract public class RootUser extends Time {
    private String version;
    private GrantType grant = GrantType.USER;
    private String access_token;
    private String email;
    private String id;
    private int no;
}
