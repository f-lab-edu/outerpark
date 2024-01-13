package com.sadowbass.outerpark.application.account.repository;

import com.sadowbass.outerpark.application.account.domain.Account;
import com.sadowbass.outerpark.application.account.dto.AccountInfo;
import com.sadowbass.outerpark.application.product.dto.MyTicket;
import com.sadowbass.outerpark.infra.utils.Pagination;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository {

    Account findByEmail(String email);

    AccountInfo findAccountInfoById(Long id);

    Long save(Account account);

    void updateCreateData(Account account);

    int findMyReservationsCount(Long memberId, LocalDate startDate);

    List<MyTicket> findMyReservations(Long memberId, LocalDate startDate, Pagination page);
}
