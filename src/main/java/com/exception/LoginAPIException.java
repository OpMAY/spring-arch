package com.exception;

import com.exception.enums.BaseExceptionType;
import lombok.Getter;

public class LoginAPIException extends RuntimeException {
    @Getter
    private BaseExceptionType exceptionType;

    public LoginAPIException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public LoginAPIException(BaseExceptionType exceptionType, Exception e) {
        super(exceptionType.getErrorMessage(), e.getCause());
        this.exceptionType = exceptionType;
    }
}
