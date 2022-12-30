package com.interceptor;

import com.util.Constant;
import com.util.Encryption.EncryptionService;
import com.util.Encryption.JWTEnum;
import com.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final EncryptionService encryptionService;

    @PostConstruct
    public void AuthInterceptor() {
        log.debug("Auth Interceptor Post Initialize");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object jwtToken = request.getSession().getAttribute(JWTEnum.JWTToken.name());
        if (jwtToken != null) {
            //로그인 정보가 있을 때
            String version = encryptionService.getSessionParameter((String) jwtToken, JWTEnum.VERSION.name());
            if (!Objects.equals(version, Constant.VERSION)) {
                //로그인 정보가 있지만 버전이 다를 때
                request.getSession().removeAttribute(JWTEnum.JWTToken.name());
                response.sendRedirect("/");
                return false;
            } else {
                //로그인 정보가 있고 버전이 같을 때 (시그니처 검사 구간, JWTToken 검증 구간)

            }
        } else {
            //로그인 정보가 없을 때
            response.sendRedirect("/");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
