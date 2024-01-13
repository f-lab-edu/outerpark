package com.sadowbass.outerpark.application.product.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sadowbass.outerpark.application.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Ticket extends BaseEntity {

    private Long id;
    private Long memberId;
    private Long roundSeatId;
    private String productName;
    private String hallName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime roundDateTime;
    private String seatName;
    private String gradeName;
    private BigDecimal price;
}
