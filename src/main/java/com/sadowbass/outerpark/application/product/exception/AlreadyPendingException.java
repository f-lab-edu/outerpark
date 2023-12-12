package com.sadowbass.outerpark.application.product.exception;

public class AlreadyPendingException extends RuntimeException {

    public static final String EXCEPTION_MESSAGE = "이미 선택된 좌석입니다.";

    public AlreadyPendingException() {
        super(EXCEPTION_MESSAGE);
    }
}
