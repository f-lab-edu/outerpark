package com.sadowbass.outerpark.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {

    private final int responseCode;
    private final String responseMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    private static final String DEFAULT_OK_MESSAGE = "ok";

    public BaseResponse(int responseCode, String responseMessage, T result) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage == null ? DEFAULT_OK_MESSAGE : responseMessage;
        this.result = result;
    }

    public static BaseResponse<Void> ok() {
        return new BaseResponse<>(HttpStatus.OK.value(), DEFAULT_OK_MESSAGE, null);
    }

    public static <T> BaseResponse<T> okWithResult(T result) {
        return new BaseResponse<>(HttpStatus.OK.value(), DEFAULT_OK_MESSAGE, result);
    }
}
