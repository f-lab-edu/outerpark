package com.sadowbass.outerpark.application.account.exception;

public class InvalidLoginInformationException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "이메일 혹은 비밀번호가 다릅니다";

    public InvalidLoginInformationException() {
        super(EXCEPTION_MESSAGE);
    }
}
