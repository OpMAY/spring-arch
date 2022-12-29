package com.exception;

import com.exception.enums.BaseExceptionType;
import lombok.Getter;

public class TokenInvalidException extends RuntimeException {
    @Getter
    private BaseExceptionType exceptionType;

    public TokenInvalidException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public TokenInvalidException(BaseExceptionType exceptionType, Exception e) {
        super(exceptionType.getErrorMessage(), e.getCause());
        this.exceptionType = exceptionType;
    }
}
