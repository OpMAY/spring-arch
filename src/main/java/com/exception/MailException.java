package com.exception;

import com.exception.enums.BaseExceptionType;
import lombok.Getter;

public class MailException extends RuntimeException {
    @Getter
    private BaseExceptionType exceptionType;

    public MailException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public MailException(BaseExceptionType exceptionType, Exception e) {
        super(exceptionType.getErrorMessage(), e.getCause());
        this.exceptionType = exceptionType;
    }
}
