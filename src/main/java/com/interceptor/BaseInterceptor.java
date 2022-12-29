package com.interceptor;

import com.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseInterceptor extends HandlerInterceptorAdapter {
    /**
     * Interceptor 사용할 경우
     * - 애플리케이션 로깅과 같은 교차 문제 처리
     * - 자세한 승인 확인 (권한 자세한 검사)
     * - Spring 컨텍스트 또는 모델 조작
     * <p>
     * 등록에 관련된 코드는 Dispatcher-servlet.xml Interceptor 부분 참조
     * <p>
     * Return false = interceptor chain stop
     * Return true = interceptor chain continue
     * PreHandler = Some Controller Before Start
     * PostHandler = Some Controller After Start
     * AfterCompletion = Some Controller After and View Complete
     * AfterConcurrentHandlingStarted = Asynchronous process (post,after func not executed)
     * <p>
     * 순서 로직
     * Filter Init -> Interceptor PostConstruct -> Filter doFilter(Pre Process) -> Interceptor preHandle ->
     * Interceptor postHandle -> Interceptor afterCompletion -> Filter(After Process)
     */

    @PostConstruct
    public void BaseInterceptor() {
        log.debug("Base Interceptor Post Initialize");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * Clear Setting
         * @Date 2022-07-20
         * @Author kimwoosik
         * Resources의 .js 파일들 및 .css 파일들의 version을 붙여서 새롭게 갱신하기 위한 코드
         * 수정될 수 있는 또는 특정한 역활을 하는 .js 파일들 및 .css 파일들의 옆에 들어가야한다.
         * @Description
         * ../../../resources/css/theme/theme.css?vc=${RESOURCES_VERSION}
         *../../../resources/js/theme/theme.js?vc=${RESOURCES_VERSION}
         * */
        request.setAttribute("RESOURCES_VERSION", Utility.RandomIntegerToken(7));
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
