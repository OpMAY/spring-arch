package com.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecoverInterceptor extends HandlerInterceptorAdapter {
    @Value("${RECOVER.URLS}")
    private String recover_urls;
    private ArrayList<String> recovers;

    @PostConstruct
    public void RecoverInterceptor() {
        recovers = new ArrayList<String>();
        if (recover_urls != null && recover_urls.length() != 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(recover_urls, ",");
            while (stringTokenizer.hasMoreTokens()) {
                recovers.add(stringTokenizer.nextToken());
            }
        }
        log.debug("Recover Interceptor Post Initialized -> urls : {}", recover_urls);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (recovers.contains(request.getRequestURI())) {
            response.sendRedirect("/test/recover");
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
