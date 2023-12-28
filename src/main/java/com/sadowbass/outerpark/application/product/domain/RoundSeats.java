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
    private String pendingId;
    private ReservationStatus status;
    private LocalDateTime expire;

    public void makeReservation(Long memberId, String pendingId) {
        this.memberId = memberId;
        this.status = ReservationStatus.PENDING;
        this.expire = LocalDateTime.now().plusMinutes(5);
        this.pendingId = pendingId;

        modify(memberId);
    }
}
