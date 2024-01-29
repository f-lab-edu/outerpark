package com.sadowbass.outerpark.presentation.controller;

import com.sadowbass.outerpark.infra.session.exception.LoginRequiredException;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginManagerControllerAdvice {

    @ExceptionHandler(LoginRequiredException.class)
    public BaseResponse<Void> handleLoginRequiredException() {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), LoginRequiredException.EXCEPTION_MESSAGE, null);
    }
}
