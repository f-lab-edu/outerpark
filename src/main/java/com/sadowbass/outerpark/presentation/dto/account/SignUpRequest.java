package com.sadowbass.outerpark.presentation.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SignUpRequest {

    @Email
    @NotNull
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
}
