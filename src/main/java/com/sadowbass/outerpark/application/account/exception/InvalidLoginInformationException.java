package com.sadowbass.outerpark.application.account.exception;

public class InvalidLoginInformationException extends RuntimeException {

    public InvalidLoginInformationException() {
        super("이메일 혹은 비밀번호가 다릅니다");
    }
}
