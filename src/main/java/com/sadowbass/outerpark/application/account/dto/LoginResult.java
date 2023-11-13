package com.sadowbass.outerpark.application.account.dto;

import lombok.Getter;

@Getter
public class LoginResult {

    private String email;

    public LoginResult(String email) {
        this.email = email;
    }
}
