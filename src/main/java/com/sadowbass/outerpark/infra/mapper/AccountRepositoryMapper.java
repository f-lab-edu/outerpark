package com.sadowbass.outerpark.infra.mapper;

import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.product.domain.Ticket;
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

    @Override
    List<Ticket> findAllTicketsByMemberIdAndTicketIds(
            @Param("memberId") Long memberId,
            @Param("ticketIds") List<Long> ticketIds
    );

    @Override
    int cancelTickets(
            @Param("ticketIds") List<Long> ticketIds,
            @Param("modifiedBy") Long modifiedBy
    );
}
