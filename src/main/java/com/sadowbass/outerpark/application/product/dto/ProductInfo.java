package com.sadowbass.outerpark.application.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductInfo {

    private Long id;
    private String hallName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime firstDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastDate;
    private String name;
    private String describe;
    private Integer runningTime;
}
