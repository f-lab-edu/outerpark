package com.sadowbass.outerpark.application.account.exception;

public class NoSuchAccountDataException extends RuntimeException {

    public NoSuchAccountDataException() {
        super("입력하신 정보의 회원이 없습니다");
    }
}
