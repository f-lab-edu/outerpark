package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.service.LoginService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private static final String SESSION_ID = "loginUser";

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        if (session.getAttribute(SESSION_ID) != null) {
            //TODO create custom exception
            throw new RuntimeException("이미 로그인 된 세션입니다.");
        }

        LoginResult loginResult = loginService.login(loginRequest);
        session.setAttribute(SESSION_ID, loginResult);

        return BaseResponse.ok();
    }
}
