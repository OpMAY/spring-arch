package com.exception.enums;

public enum GlobalExceptionType implements BaseExceptionType {
    GRANT_EXCEPTION(1001, 200, "1001, Login or Permission Exception"),
    TOKEN_EXCEPTION(1002, 200, "1002, JWT Token or Token Invalid Exception"),
    MAIL_EXCEPTION(1003, 200, "1003, Mail Builder Exception"),
    LOGIN_API_EXCEPTION(1004, 200, "1004, SNS Login Exception"),
    NAVER_API_EXCEPTION(1005, 200, "1005, Naver API Exception"),
    KAKAO_API_EXCEPTION(1006, 200, "1006, Kakao API Exception"),
    GOOGLE_API_EXCEPTION(1007, 200, "1007, Google API Exception");

    private int errorCode;
    private int httpStatus;
    private String errorMessage;

    GlobalExceptionType(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
