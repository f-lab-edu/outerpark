package com.sadowbass.outerpark.application.product.repository;

import com.sadowbass.outerpark.application.product.dto.ProductInfo;

public interface ProductRepository {

    ProductInfo findProductInfoByProductId(Long productId);
}
