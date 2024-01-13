package com.sadowbass.outerpark.presentation.controller.account;

import com.sadowbass.outerpark.application.account.dto.AccountInfo;
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.service.AccountService;
import com.sadowbass.outerpark.application.product.dto.MyTicket;
import com.sadowbass.outerpark.infra.utils.Pagination;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.PageResult;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/me/reservations")
    public BaseResponse<PageResult<MyTicket>> myReservations(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "startDate", required = false) LocalDate startDate,
            Pagination pagination
    ) {
        PageResult<MyTicket> pageResult = accountService.retrieveMyReservations(startDate, pagination);
        return BaseResponse.okWithResult(pageResult);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public BaseResponse<Void> handleDuplicateEmailException(DuplicateEmailException duplicateEmailException) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), duplicateEmailException.getMessage(), null);
    }
}
