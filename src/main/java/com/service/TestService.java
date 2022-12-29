package com.service;

import com.dao.TestDao;
import com.exception.GrantAccessDeniedException;
import com.exception.enums.GlobalExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
    private final TestDao testDao;

    public void testException() {
        if (true) {
            throw new GrantAccessDeniedException(GlobalExceptionType.GRANT_EXCEPTION);
        }
    }
}