package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.service.AccountService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public BaseResponse<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        accountService.signUp(signUpRequest);
        return BaseResponse.ok();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public BaseResponse<Void> handleDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), duplicateEmailException.getMessage(), null);
    }
}
