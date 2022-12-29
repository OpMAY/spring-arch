package com.api.sns.naver;

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
import java.net.URLEncoder;
import java.util.HashMap;

@Service
@Slf4j
public class NaverAPI {
    @Value("${NAVER.CLIENT_ID}")
    private String NAVER_CLIENT_ID;
    @Value("${NAVER.CLIENT_SECRET}")
    private String NAVER_CLIENT_SECRET;
    private final String NAVER_OAUTH_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_USER_INFO_REQUEST_URL = "https://openapi.naver.com/v1/nid/me";
    private final String NAVER_LOGOUT_REQUEST_URL = "https://nid.naver.com/oauth2.0/token";
    private NaverAccess naverAccess;
    private NaverInfo naverInfo;

    @PostConstruct
    public void NaverAPI() {
    }

    public NaverAccess getAccessToken(HttpServletRequest req, String authorize_code, String state) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", NAVER_CLIENT_ID);
            params.put("client_secret", NAVER_CLIENT_SECRET);
            params.put("redirect_uri", URLEncoder.encode(req.getRequestURL().toString(), "UTF-8"));
            params.put("code", authorize_code);
            params.put("state", state);
            StringBuilder stringBuilder = ProtocolBuilderHelper.setGetMethodParams(params);
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(NAVER_OAUTH_TOKEN_URL + stringBuilder.toString())
                    .conn()
                    .setRequestMethod("GET");
            naverAccess = (NaverAccess) protocolBuilder.openReader("UTF-8", NaverAccess.class, true);
            return naverAccess;
        } catch (UnsupportedEncodingException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        }
    }

    public NaverInfo getUser(String access_Token) {
        try {
            HashMap<String, String> properties = new HashMap<>();
            properties.put("Authorization", "Bearer " + access_Token);
            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(NAVER_USER_INFO_REQUEST_URL)
                    .conn()
                    .setRequestMethod("GET")
                    .setRequestProperty(properties);
            naverInfo = (NaverInfo) protocolBuilder.openReader("UTF-8", NaverInfo.class, true);
            return naverInfo;
        } catch (MalformedURLException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        }
    }

    public NaverAccess logout(String access_Token) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("grant_type", "delete");
            params.put("client_id", NAVER_CLIENT_ID);
            params.put("client_secret", NAVER_CLIENT_SECRET);
            params.put("access_token", access_Token);
            params.put("service_provider", "NAVER");

            HashMap<String, String> properties = new HashMap<>();
            properties.put("Authorization", "Bearer " + access_Token);

            StringBuilder stringBuilder = ProtocolBuilderHelper.setGetMethodParams(params);

            ProtocolBuilder protocolBuilder = new ProtocolBuilder()
                    .url(NAVER_LOGOUT_REQUEST_URL + stringBuilder.toString())
                    .conn()
                    .setRequestMethod("GET")
                    .setRequestProperty(properties);
            NaverAccess naverAccess = (NaverAccess) protocolBuilder.openReader("UTF-8", NaverAccess.class, true);
            return naverAccess;
        } catch (IOException e) {
            throw new LoginAPIException(GlobalExceptionType.NAVER_API_EXCEPTION);
        }
    }
}
