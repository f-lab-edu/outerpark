package com.sadowbass.outerpark.presentation.dto;

import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {

    private final Object detail;

    public ErrorResponse(int responseCode, String responseMessage, Object detail) {
        super(responseCode, responseMessage);
        this.detail = detail;
    }
}
