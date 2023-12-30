package com.sadowbass.outerpark.application.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

    private String email;
    private String name;
    private String nickname;
    private String phone;
}
