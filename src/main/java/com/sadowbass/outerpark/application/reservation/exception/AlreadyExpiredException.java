package com.sadowbass.outerpark.application.reservation.exception;

public class AlreadyExpiredException extends RuntimeException{

    public static final String EXCEPTION_MESSAGE = "결제 가능 기한을 초과하였습니다.";

    public AlreadyExpiredException() {
        super(EXCEPTION_MESSAGE);
    }
}
