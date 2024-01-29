package com.sadowbass.outerpark.application.myinfo.service;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.myinfo.dto.MyInfo;
import com.sadowbass.outerpark.application.myinfo.dto.MyTicket;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.Pagination;
import com.sadowbass.outerpark.presentation.dto.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyInfoService {

    private final LoginManager loginManager;
    private final AccountRepository accountRepository;

    public MyInfo retrieveMyInfo() {
        LoginResult member = loginManager.getMember();
        return accountRepository.findMyInfoById(member.getId());
    }

    public PageResult<MyTicket> retrieveMyReservations(LocalDate startDate, Pagination pagination) {
        LoginResult member = loginManager.getMember();
        int totalCount = accountRepository.findMyReservationsCount(member.getId(), startDate);
        List<MyTicket> myReservations = accountRepository.findMyReservations(member.getId(), startDate, pagination);

        return new PageResult<>(pagination, totalCount, myReservations);
    }
}
