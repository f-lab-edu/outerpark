package com.sadowbass.outerpark.infra.mapper;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductRepositoryMapper extends ProductRepository {

    @Override
    List<AvailableSeat> findAvailableSeatByRoundIdAndGradeId(@Param("roundId") Long roundId, @Param("gradeId") Long gradeId);
}
