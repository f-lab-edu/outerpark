package com.sadowbass.outerpark.application.product.exception;

public class NoSuchProductException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "존재하지 않는 공연입니다.";

    public NoSuchProductException() {
        super(EXCEPTION_MESSAGE);
    }
}
