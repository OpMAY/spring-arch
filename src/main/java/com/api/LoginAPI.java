package com.api;

import com.api.sns.google.GoogleAPI;
import com.api.sns.google.GoogleAccess;
import com.api.sns.google.GoogleInfo;
import com.api.sns.kakao.KakaoAPI;
import com.api.sns.kakao.KakaoAccess;
import com.api.sns.kakao.KakaoInfo;
import com.api.sns.naver.NaverAPI;
import com.api.sns.naver.NaverAccess;
import com.api.sns.naver.NaverInfo;
import com.model.User;
import com.model.common.MFile;
import com.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAPI {
    private final KakaoAPI kakaoAPI;
    private final NaverAPI naverAPI;
    private final GoogleAPI googleAPI;

    @Value("${DEFAULT.PROFILE.IMAGE.URL}")
    private String DEFAULT_PROFILE_IMAGE_URL;
    @Value("${DEFAULT.PROFILE.IMAGE.NAME}")
    private String DEFAULT_PROFILE_IMAGE_NAME;
    @Value("${DEFAULT.PROFILE.IMAGE.SIZE}")
    private String DEFAULT_PROFILE_IMAGE_SIZE;
    @Value("${DEFAULT.PROFILE.IMAGE.TYPE}")
    private String DEFAULT_PROFILE_IMAGE_TYPE;

    /**
     * @param req : req (request)
     * @return : void (void)
     * Description : Login API를 이용하여 로그인 했을 때 결과를 받는 함수
     * Date : 2020-10-10
     * Updated date : 2020-10-12 Kakao Login 추가
     * Updated date : 2020-10-13 Naver Login 추가
     * Updated date : 2020-11-17 Naver Login 제거, Login Logic 추가
     * Version : 4
     */
    public User apiLoginInit(HttpServletRequest req) {
        User user = new User();
        MFile profile_img = new MFile();
        /** Kakao Naver Google Login Get Auth Code And Error Code = code in Common*/
        String code = req.getParameter("code");
        /** Kakao Login Get Auth = error*/
        String error = req.getParameter("error");
        /** Naver Google Login Get Auth = state*/
        String state = req.getParameter("state");
        /** Google Login Get Auth = scope*/
        String scope = req.getParameter("scope");

        /** Naver*/
        if (code != null && state != null && scope == null) {
            /** Naver Login Success*/
            System.out.println("Naver Code : " + code);
            NaverAccess naverAccess = naverAPI.getAccessToken(req, code, state);
            /** Naver Login Get Token*/
            NaverInfo naverInfo = naverAccess.getAccess_token() != null ? naverAPI.getUser(naverAccess.getAccess_token()) : null;
            if (naverInfo == null) {
                /**Login Failed*/
                return null;
            }
            /** Login Success*/
            user.setEmail(naverInfo.getResponse().getEmail());
            user.setId(naverInfo.getResponse().getId());
            user.setName(naverInfo.getResponse().getNickname());
            profile_img.setUrl(naverInfo.getResponse().getProfile_image() != null ? naverInfo.getResponse().getProfile_image() : null);
            if (profile_img.getUrl() == null) {
                profile_img.setUrl(DEFAULT_PROFILE_IMAGE_URL);
                profile_img.setName(DEFAULT_PROFILE_IMAGE_NAME);
                profile_img.setSize(Long.parseLong(DEFAULT_PROFILE_IMAGE_SIZE));
                profile_img.setType(DEFAULT_PROFILE_IMAGE_TYPE);
            }
            user.setProfile_img(profile_img);
            return user;
        }

        /** Kakao*/
        if (code != null && state == null && scope == null) {
            /** Kakao Login success*/
            /** Kakao Login Get Token*/
            KakaoAccess kakaoAccess = kakaoAPI.getAccessToken(req, code);
            if (kakaoAccess == null) {
                /* Login Failed*/
                return null;
            }
            /** Kakao Login Get User*/
            KakaoInfo kakaoInfo = kakaoAPI.getUser(kakaoAccess.getAccess_token());
            //    클라이언트의 이메일이 존재할 때 세션에 해당 이메일과 토큰 등록
            if (kakaoInfo == null) {
                /** Login Failed*/
                return null;
            }
            /** Login Success*/

            if (kakaoInfo.getKakao_account().getHas_email() != null && kakaoInfo.getKakao_account().getHas_email()) {
                user.setEmail(kakaoInfo.getKakao_account().getEmail());
            }
            user.setId(Long.toString(kakaoInfo.getId()));
            user.setName(
                    kakaoInfo.getKakao_account() != null ? kakaoInfo.getKakao_account().getProfile().getNickname() != null ?
                            kakaoInfo.getKakao_account().getProfile().getNickname() :
                            kakaoInfo.getProperties() != null ? kakaoInfo.getProperties().getNickname() : null : null);
            if (user.getName() == null) {
                user.setName("user" + Utility.RandomIntegerToken(4));
            }
            profile_img.setUrl(kakaoInfo.getKakao_account() != null ? kakaoInfo.getKakao_account().getProfile().getProfile_image_url() : kakaoInfo.getProperties().getProfile_image() != null ? kakaoInfo.getProperties().getProfile_image() : null);
            if (profile_img.getUrl() == null) {
                profile_img.setUrl(DEFAULT_PROFILE_IMAGE_URL);
                profile_img.setName(DEFAULT_PROFILE_IMAGE_NAME);
                profile_img.setSize(Long.parseLong(DEFAULT_PROFILE_IMAGE_SIZE));
                profile_img.setType(DEFAULT_PROFILE_IMAGE_TYPE);
            }
            user.setProfile_img(profile_img);
            return user;
        }

        /** Google*/
        else if (code != null && state == null && scope != null) {
            /** Google Login Success*/
            code = req.getParameter("code");
            /** Google Token 3 Types
             * first : access_token(logout token)
             * second : refresh token
             * third : id_token(in this is real token to get profile token)*/
            GoogleAccess googleAccess = code != null ? googleAPI.getAccessToken(req, code) : null;
            if (googleAccess == null) {
                /* Login Failed*/
                return null;
            }
            GoogleInfo googleInfo = code != null ? googleAPI.getUser(googleAccess.getAccess_token()) : null;
            if (googleInfo == null) {
                /** Login Failed*/
                return null;
            }
            /** Login Success*/
            /**Google API는 NAME 못가져옵니다. 다른 방법을 사용해서 얻어야합니다.*/
            user.setEmail(googleInfo.getEmail());
            user.setId(googleInfo.getId());
            user.setName(googleInfo.getName() == null ? "Empty User Name" : googleInfo.getName());
            profile_img.setUrl(googleInfo.getPicture() != null ? googleInfo.getPicture() : null);
            if (profile_img.getUrl() == null) {
                profile_img.setUrl(DEFAULT_PROFILE_IMAGE_URL);
                profile_img.setName(DEFAULT_PROFILE_IMAGE_NAME);
                profile_img.setSize(Long.parseLong(DEFAULT_PROFILE_IMAGE_SIZE));
                profile_img.setType(DEFAULT_PROFILE_IMAGE_TYPE);
            }
            user.setProfile_img(profile_img);
            return user;
        } else {
            if (error != null) {
                /**Kakao Login Error
                 * User denied access	사용자가 동의 화면에서 동의하고 시작하기를 누르지 않고 로그인을 취소한 경우
                 * Not allowed under age 14	만14세 미만 사용자의 보호자 동의 실패*/
                /**Google Login Error
                 * https://oauth2.example.com/callback#error=access_denied
                 * */
                /** Kakao Login Error*/
                /** Google Login Error*/
                return null;
            }
        }
        return null;
    }
}
