package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.common.MFile;
import com.model.jwt.RootUser;
import lombok.*;

@RequiredArgsConstructor
@Data
@AllArgsConstructor
@JsonIgnoreProperties
@ToString(callSuper = true)
public class User extends RootUser {
    private String name;
    private String access_token = "setting the password";
    private MFile profile_img;
}
