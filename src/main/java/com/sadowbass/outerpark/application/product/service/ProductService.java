package com.sadowbass.outerpark.application.product.service;

import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfo findProductInfoByProductId(Long productId) {
        ProductInfo productInfo = productRepository.findProductInfoByProductId(productId);
        if (productInfo == null) {
            throw new NoSuchProductException();
        }

        return productInfo;
    }
}
