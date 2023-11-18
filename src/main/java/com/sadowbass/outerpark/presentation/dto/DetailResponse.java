package com.sadowbass.outerpark.presentation.dto;

import lombok.Getter;

@Getter
public class DetailResponse extends BaseResponse {

    private final Object detail;

    public DetailResponse(int responseCode, String responseMessage, Object detail) {
        super(responseCode, responseMessage);
        this.detail = detail;
    }
}
