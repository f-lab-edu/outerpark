package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.dto.AccountInfo;
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.service.AccountService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    public BaseResponse<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        accountService.signUp(signUpRequest);
        return BaseResponse.ok();
    }

    @GetMapping("/me")
    public BaseResponse<AccountInfo> myInfo() {
        AccountInfo accountInfo = accountService.retrieveMyInfo();
        return BaseResponse.okWithResult(accountInfo);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public BaseResponse<Void> handleDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), duplicateEmailException.getMessage(), null);
    }
}
