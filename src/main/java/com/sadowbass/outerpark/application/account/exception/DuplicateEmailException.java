package com.sadowbass.outerpark.application.account.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "이미 존재하는 이메일입니다. : ";

    public DuplicateEmailException(String email) {
        super(EXCEPTION_MESSAGE + email);
    }
}
