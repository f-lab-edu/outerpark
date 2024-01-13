package com.sadowbass.outerpark.infra.mapper;

import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.product.dto.MyTicket;
import com.sadowbass.outerpark.infra.utils.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AccountRepositoryMapper extends AccountRepository {

    @Override
    int findMyReservationsCount(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate
    );

    @Override
    List<MyTicket> findMyReservations(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("page") Pagination page
    );
}
