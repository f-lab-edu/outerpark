package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.exception.AlreadyLoggedInException;
import com.sadowbass.outerpark.application.account.exception.InvalidLoginInformationException;
import com.sadowbass.outerpark.application.account.exception.NoSuchAccountDataException;
import com.sadowbass.outerpark.application.account.service.LoginService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest loginRequest) {
        loginService.login(loginRequest);

        return BaseResponse.ok();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AlreadyLoggedInException.class, NoSuchAccountDataException.class, InvalidLoginInformationException.class})
    public BaseResponse handleAlreadyLoggedInException(RuntimeException exception) {
        return new BaseResponse(400, exception.getMessage());
    }
}