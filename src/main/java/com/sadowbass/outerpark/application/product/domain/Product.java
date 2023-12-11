package com.sadowbass.outerpark.application.product.domain;

import com.sadowbass.outerpark.application.BaseEntity;
import lombok.Getter;

@Getter
public class Product extends BaseEntity {

    private Long id;
    private Long hallId;
    private String name;
    private String describe;
    private Integer runningTime;
}
