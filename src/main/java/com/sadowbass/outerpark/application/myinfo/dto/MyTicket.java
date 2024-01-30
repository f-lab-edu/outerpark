package com.sadowbass.outerpark.application.myinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sadowbass.outerpark.application.product.domain.TicketStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MyTicket {

    private Long id;
    private String productName;
    private String hallName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime performanceDateTime;
    private String seatName;
    private String gradeName;
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate;
    private TicketStatus ticketStatus;
}
