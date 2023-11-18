package com.sadowbass.outerpark.infra.repository;

import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductRepositoryMapper extends ProductRepository {
}
