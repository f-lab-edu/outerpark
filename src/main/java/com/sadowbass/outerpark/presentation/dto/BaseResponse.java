package com.sadowbass.outerpark.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse {

    private int responseCode;
    private String responseMessage;

    public static BaseResponse ok() {
        return new BaseResponse(200, "ok");
    }
}
