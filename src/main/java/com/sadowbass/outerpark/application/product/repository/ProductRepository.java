package com.sadowbass.outerpark.application.product.repository;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import lombok.extern.java.Log;

import java.util.List;

public interface ProductRepository {

    ProductInfo findProductInfoByProductId(Long productId);

    List<RoundInfo> findRoundInfosByProductId(Long productId);

    List<AvailableSeat> findAvailableSeatByRoundIdAndGradeId(Long roundId, Long gradeId);
}
