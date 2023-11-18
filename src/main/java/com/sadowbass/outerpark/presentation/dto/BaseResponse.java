package com.sadowbass.outerpark.presentation.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse {

    private final int responseCode;
    private final String responseMessage;

    private static final String DEFAULT_OK_MESSAGE = "ok";

    public BaseResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage == null ? DEFAULT_OK_MESSAGE : responseMessage;
    }

    public static BaseResponse ok() {
        return new BaseResponse(HttpStatus.OK.value(), "ok");
    }
}
