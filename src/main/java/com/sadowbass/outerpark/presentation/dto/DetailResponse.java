package com.sadowbass.outerpark.presentation.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DetailResponse extends BaseResponse {

    private final Object detail;

    public DetailResponse(int responseCode, String responseMessage, Object detail) {
        super(responseCode, responseMessage);
        this.detail = detail;
    }

    public static DetailResponse ok(Object detail) {
        return new DetailResponse(HttpStatus.OK.value(), DEFAULT_OK_MESSAGE, detail);
    }
}
