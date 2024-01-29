package com.sadowbass.outerpark.infra.mapper;

import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductRepositoryMapper extends ProductRepository {

    @Override
    List<AvailableSeat> findAvailableSeatByRoundIdAndGradeId(
            @Param("roundId") Long roundId,
            @Param("gradeId") Long gradeId
    );

    @Override
    List<RoundSeats> findEnabledRoundSeatsByRoundIdAndSeatIds(
            @Param("roundId") Long roundId,
            @Param("seats") List<Long> seats
    );

    @Override
    int pendingRoundSeats(@Param("seats") List<RoundSeats> enableRoundSeats);

    @Override
    List<RoundSeats> findPendingRoundSeats(
            @Param("memberId") Long memberId,
            @Param("roundId") Long roundId,
            @Param("pendingId") String pendingId
    );

    @Override
    int reserveRoundSeats(
            @Param("seats") List<Long> seats,
            @Param("memberId") Long memberId
    );

    @Override
    int createTickets(
            @Param("seats") List<Long> seats,
            @Param("memberId") Long memberId,
            @Param("roundId") Long roundId
    );

    @Override
    int returnRoundSeatsToEnable(
            @Param("roundSeatIds") List<Long> roundSeatIds,
            @Param("modifiedBy") Long modifiedBy
    );
}
