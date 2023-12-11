package com.sadowbass.outerpark.application.account.exception;

public class AlreadyLoggedInException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "이미 로그인 된 사용자 입니다";

    public AlreadyLoggedInException() {
        super(EXCEPTION_MESSAGE);
    }
}
