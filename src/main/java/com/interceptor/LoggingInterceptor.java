package com.interceptor;

import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface LoggingInterceptor {
    Map<String, Object> getHeaders(ContentCachingRequestWrapper request);

    boolean isVisible(MediaType mediaType);

    String getRequestBody(ContentCachingRequestWrapper request);

    String getParameterMap(Map<String, String[]> parameterMap) throws IOException;
}
