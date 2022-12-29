package com.config;

import com.util.Constant;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
public class SessionConfig implements HttpSessionListener {

    @NonNull
    private int sessionInterval = Constant.SESSION_INTERVAL;

    public SessionConfig(int sessionInterval) {
        if (sessionInterval != 0) {
            this.sessionInterval = sessionInterval;
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setMaxInactiveInterval(this.sessionInterval);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}
