package com.sadowbass.outerpark.presentation.controller;

import com.sadowbass.outerpark.infra.utils.validation.ViolationResult;
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public BaseResponse<List<ViolationResult>> handleValidation(ValidationException validationException) {
        return new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ValidationException.EXCEPTION_MESSAGE,
                validationException.getViolationResult()
        );
    }
}
