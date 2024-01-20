package com.sadowbass.outerpark.application.product.repository;

import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;

import java.util.List;

public interface ProductRepository {

    ProductInfo findProductInfoByProductId(Long productId);

    List<RoundInfo> findRoundInfosByProductId(Long productId);

    List<AvailableSeat> findAvailableSeatByRoundIdAndGradeId(Long roundId, Long gradeId);

    List<RoundSeats> findEnabledRoundSeatsByRoundIdAndSeatIds(Long roundId, List<Long> seats);

    int pendingRoundSeats(List<RoundSeats> enableRoundSeats);

    List<RoundSeats> findPendingRoundSeats(Long memberId, Long roundId, String pendingId);

    int reserveRoundSeats(List<Long> seats, Long id);

    int createTickets(List<Long> seats, Long memberId, Long roundId);

    int returnRoundSeatsToEnable(List<Long> roundSeatIds, Long modifiedBy);
}