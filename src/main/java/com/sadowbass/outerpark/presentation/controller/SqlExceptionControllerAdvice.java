package com.sadowbass.outerpark.presentation.controller;

import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class SqlExceptionControllerAdvice {

    private static final String UNIQUE_VIOLATION_MESSAGE = "이메일이 중복되었습니다.";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public BaseResponse handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        return new BaseResponse(HttpStatus.BAD_REQUEST.value(), UNIQUE_VIOLATION_MESSAGE);
    }
}
