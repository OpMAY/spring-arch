package com.exception;

import com.exception.enums.BaseExceptionType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrantAccessDeniedException extends RuntimeException {
    @Getter
    private BaseExceptionType exceptionType;

    public GrantAccessDeniedException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public GrantAccessDeniedException(BaseExceptionType exceptionType, Exception e) {
        super(exceptionType.getErrorMessage(), e.getCause());
        this.exceptionType = exceptionType;
    }
}
