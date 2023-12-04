package com.sadowbass.outerpark.application.account;

import com.sadowbass.outerpark.application.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Account extends BaseEntity {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;

    private Account(String email, String password, String name, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }

    public static Account create(String email, String password, String name, String nickname, String phone) {
        return new Account(email, password, name, nickname, phone);
    }
}
