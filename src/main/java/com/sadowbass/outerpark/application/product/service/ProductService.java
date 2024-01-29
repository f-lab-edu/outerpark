package com.sadowbass.outerpark.application.product.service;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo findProductInfoByProductId(Long productId) {
        ProductInfo productInfo = productRepository.findProductInfoByProductId(productId);
        if (productInfo == null) {
            throw new NoSuchProductException();
        }

        return productInfo;
    }

    public List<RoundInfo> findRoundInfosByProductId(Long productId) {
        return productRepository.findRoundInfosByProductId(productId);
    }

    public List<AvailableSeat> findAvailableSeatsByRoundIdAndGradeId(Long roundId, Long gradeId) {
        return productRepository.findAvailableSeatByRoundIdAndGradeId(roundId, gradeId);
    }
}