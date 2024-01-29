package com.sadowbass.outerpark.infra.session.exception;

public class LoginRequiredException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "로그인이 필요합니다.";

    public LoginRequiredException() {
        super(EXCEPTION_MESSAGE);
    }
}
