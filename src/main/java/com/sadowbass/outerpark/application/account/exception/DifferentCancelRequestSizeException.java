package com.sadowbass.outerpark.application.account.exception;

public class DifferentCancelRequestSizeException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "취소 요청좌석 숫자와 취소가능한 좌석 숫자가 다릅니다.";

    public DifferentCancelRequestSizeException() {
        super(EXCEPTION_MESSAGE);
    }
}
