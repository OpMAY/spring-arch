package com.api.sns.kakao;

import com.exception.LoginAPIException;
import com.exception.enums.GlobalExceptionType;
import com.transfer.ProtocolBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

@Service
@Slf4j
public class KakaoAPI {
    @Value("${KAKAO.CLIENT_ID}")
    private String KAKAO_CLIENT_ID;
    @Value("${KAKAO.CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    private final String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    private final String KAKAO_LOGOUT_REQUEST_URL = "https://kapi.kakao.com/v1/user/logout";
    private KakaoAccess kakaoAccess;
    private KakaoInfo kakaoInfo;

    @PostConstruct
    public void KakaoAPI() {
    }

    /**
     * Kakao OAuth2.0 Token Getter
     * openReader return {"access_token":"access_token","token_type":"bearer","refresh_token":"refresh_token","expires_in":21599,"scope":"account_email profile","refresh_token_expires_in":5183999}
     */

    public KakaoAccess getAccessToken(HttpServletRequest req, String authorize_code) {
        try {
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(KAKAO_OAUTH_TOKEN_URL)
                    .conn()
                    .setRequestMethod("POST")
                    .setDoOutput(true);

            HashMap<String, Object> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", KAKAO_CLIENT_ID);
            params.put("client_secret", KAKAO_CLIENT_SECRET);
            params.put("redirect_uri", req.getRequestURL());
            params.put("code", authorize_code);

            protocolBuilder.openWriter(params);
            kakaoAccess = protocolBuilder.openReader("UTF-8", KakaoAccess.class, true);
            return kakaoAccess;
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        }
    }

    /**
     * Kakao User Get Information
     */
    public KakaoInfo getUser(String access_Token) {
        try {
            HashMap properties = new HashMap<String, String>();
            properties.put("Authorization", "Bearer " + access_Token);
            properties.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(KAKAO_USER_INFO_REQUEST_URL)
                    .conn()
                    .setRequestMethod("POST")
                    .setRequestProperty(properties);
            kakaoInfo = protocolBuilder.openReader("UTF-8", KakaoInfo.class, true);
            return kakaoInfo;
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        }
    }

    /**
     * Kakao Logout
     */
    public String logout(String access_Token) {
        try {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("Authorization", "Bearer " + access_Token);

            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(KAKAO_LOGOUT_REQUEST_URL)
                    .conn()
                    .setRequestMethod("POST")
                    .setRequestProperty(properties);

            String id = protocolBuilder.openReader("UTF-8", true);
            return id;
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.KAKAO_API_EXCEPTION);
        }
    }
}
