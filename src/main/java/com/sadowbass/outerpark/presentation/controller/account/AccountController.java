package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.service.AccountService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public BaseResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        accountService.signUp(signUpRequest);
        return BaseResponse.ok();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public BaseResponse handleDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        return new BaseResponse(400, duplicateEmailException.getMessage());
    }
}
