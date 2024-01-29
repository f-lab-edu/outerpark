package com.sadowbass.outerpark.presentation.controller.myinfo;

import com.sadowbass.outerpark.application.myinfo.dto.MyInfo;
import com.sadowbass.outerpark.application.myinfo.dto.MyTicket;
import com.sadowbass.outerpark.application.myinfo.service.MyInfoService;
import com.sadowbass.outerpark.infra.utils.Pagination;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class MyInfoController {

    private final MyInfoService myInfoService;

    @GetMapping("/me")
    public BaseResponse<MyInfo> myInfo() {
        MyInfo myInfo = myInfoService.retrieveMyInfo();
        return BaseResponse.okWithResult(myInfo);
    }

    @GetMapping("/me/reservations")
    public BaseResponse<PageResult<MyTicket>> myReservations(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "startDate", required = false) LocalDate startDate,
            Pagination pagination
    ) {
        PageResult<MyTicket> pageResult = myInfoService.retrieveMyReservations(startDate, pagination);
        return BaseResponse.okWithResult(pageResult);
    }
}
