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
    List<AvailableSeat> findAvailableSeatByRoundIdAndGradeId(@Param("roundId") Long roundId, @Param("gradeId") Long gradeId);

    @Override
    List<RoundSeats> findEnabledRoundSeatsByRoundIdAndSeatIds(@Param("roundId") Long roundId, @Param("seats") List<Long> seats);

    @Override
    int updateRoundSeats(@Param("seats") List<RoundSeats> enableRoundSeats);
}
