package com.sadowbass.outerpark.application.account.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginResult implements Serializable {

    private final String email;

    public LoginResult(String email) {
        this.email = email;
    }
}
