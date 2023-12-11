package com.sadowbass.outerpark.application.account.exception;

public class NoSuchAccountDataException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "입력하신 정보의 회원이 없습니다";

    public NoSuchAccountDataException() {
        super(EXCEPTION_MESSAGE);
    }
}
