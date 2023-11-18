package com.sadowbass.outerpark.presentation.controller;

import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException;
import com.sadowbass.outerpark.presentation.dto.DetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public DetailResponse handleValidation(ValidationException validationException) {
        return new DetailResponse(
                HttpStatus.BAD_REQUEST.value(),
                ValidationException.EXCEPTION_MESSAGE,
                validationException.getViolationResult()
        );
    }
}
