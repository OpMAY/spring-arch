package com.exception;

import com.exception.enums.BaseExceptionType;
import lombok.Getter;

public class CryptoException extends RuntimeException {
    @Getter
    private BaseExceptionType exceptionType;

    public CryptoException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public CryptoException(BaseExceptionType exceptionType, Exception e) {
        super(exceptionType.getErrorMessage(), e.getCause());
        this.exceptionType = exceptionType;
    }
}
