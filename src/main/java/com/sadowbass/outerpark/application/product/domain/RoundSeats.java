package com.sadowbass.outerpark.application.product.domain;

import com.sadowbass.outerpark.application.BaseEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoundSeats extends BaseEntity {

    private Long id;
    private Long memberId;
    private Long gradeId;
    private Long roundId;
    private Long seatId;
    private Status status;
    private LocalDateTime expire;

    public void makeReservation(Long memberId) {
        this.memberId = memberId;
        this.status = Status.PENDING;
        this.expire = LocalDateTime.now().plusMinutes(5);

        modify(memberId);
    }
}
