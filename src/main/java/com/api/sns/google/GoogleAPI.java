package com.api.sns.google;

import com.exception.LoginAPIException;
import com.exception.enums.GlobalExceptionType;
import com.transfer.ProtocolBuilder;
import com.transfer.ProtocolBuilderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.util.HashMap;

@Service
@Slf4j
public class GoogleAPI {
    @Value("${GOOGLE.CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;
    @Value("${GOOGLE.CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;
    private final String GOOGLE_OAUTH_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private final String GOOGLE_USER_INFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private final String GOOGLE_LOGOUT_REQUEST_URL = "https://accounts.google.com/o/oauth2/revoke";
    private GoogleAccess googleAccess;
    private GoogleInfo googleInfo;

    @PostConstruct
    public void GoogleAPI() {
    }

    public GoogleAccess getAccessToken(HttpServletRequest req, String code) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", GOOGLE_CLIENT_ID);
            params.put("client_secret", GOOGLE_CLIENT_SECRET);
            params.put("redirect_uri", URLEncoder.encode(req.getRequestURL().toString(), "UTF-8"));
            params.put("grant_type", "authorization_code");

            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(GOOGLE_OAUTH_TOKEN_URL)
                    .conn()
                    .setRequestMethod("POST")
                    .setDoOutput(true);
            protocolBuilder.openWriter(params);
            googleAccess = (GoogleAccess) protocolBuilder.openReader("UTF-8", GoogleAccess.class, true);
            return googleAccess;
        } catch (UnsupportedEncodingException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        }
    }

    public GoogleInfo getUser(String access_token) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("access_token", access_token);
            StringBuilder stringBuilder = ProtocolBuilderHelper.setGetMethodParams(params);
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(GOOGLE_USER_INFO_REQUEST_URL + stringBuilder.toString())
                    .conn()
                    .setRequestMethod("GET");
            googleInfo = (GoogleInfo) protocolBuilder.openReader("UTF-8", GoogleInfo.class, true);
            return googleInfo;
        } catch (UnsupportedEncodingException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        }
    }

    public String logout(String access_token) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", access_token);
            StringBuilder stringBuilder = ProtocolBuilderHelper.setGetMethodParams(params);
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(GOOGLE_LOGOUT_REQUEST_URL + stringBuilder.toString())
                    .conn()
                    .setRequestMethod("GET");
            String result = protocolBuilder.openReader("UTF-8", true);
            return result;
        } catch (ProtocolException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.GOOGLE_API_EXCEPTION);
        }
    }
}
