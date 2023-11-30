package com.sadowbass.outerpark.application.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class LoginResult implements Serializable {

    private Long id;
    private String email;

    public LoginResult(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
