package com.interceptor;

import com.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * PR
 *
 * @Date 2022-07-26
 * @Author kimwoosik
 * @Description Log Interceptor 추가
 * Post 및 Get URL 및 Method 및 Body 데이터 로깅
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LogInterceptor extends HandlerInterceptorAdapter implements LoggingInterceptor {
    @PostConstruct
    public void LogInterceptor() {
        log.debug("Log Interceptor Post Initialize");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            if (Constant.LogSetting.HEADER_LOG)
                log.info("request header: {}", getHeaders(requestWrapper));

            if (Constant.LogSetting.PARAMETER_LOG)
                log.info("request parameter: {}", getParameterMap(requestWrapper.getParameterMap()));

            if (Constant.LogSetting.REQUEST_BODY_LOG)
                log.info("request body: {}", getRequestBody(requestWrapper));
        } catch (Exception e) {
            log.info("request body is multipart file type, so do not support log");
        }
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    @Override
    public Map<String, Object> getHeaders(ContentCachingRequestWrapper request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    @Override
    public boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"), MediaType.MULTIPART_FORM_DATA);
        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    @Override
    public String getRequestBody(ContentCachingRequestWrapper request) {
        if (request != null) {
            byte[] buf = request.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String requestBodyStr = new String(buf, 0, buf.length, request.getCharacterEncoding());
                    return requestBodyStr;
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    @Override
    public String getParameterMap(Map<String, String[]> parameterMap) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            ArrayList<String> values = new ArrayList<>();
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (value.length > 1) {
                for (int i = 0; i < value.length; i++) {
                    values.add(value[i]);
                }
                jsonObject.put(key, values);
            } else {
                jsonObject.put(key, value[0]);
            }
            jsonArray.put(jsonObject);
        }
        if (jsonArray.isEmpty()) {
            return "false";
        } else {
            return jsonArray.toString();
        }
    }
}
