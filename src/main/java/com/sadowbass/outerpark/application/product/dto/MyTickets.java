package com.sadowbass.outerpark.application.product.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MyTickets {

    private List<MyTicket> reservations;
    private int pageNum;
    private boolean isLast;
}
