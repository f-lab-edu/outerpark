package com.sadowbass.outerpark.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseResponse {

    private int responseCode;
    private String responseMessage;

    public static BaseResponse ok() {
        return new BaseResponse(HttpStatus.OK.value(), "ok");
    }
}
