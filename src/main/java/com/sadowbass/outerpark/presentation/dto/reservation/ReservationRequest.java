package com.sadowbass.outerpark.presentation.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ReservationRequest {

    @Size(min = 1, max = 4, message = "최대 예매 가능한 수량은 1석에서 4석까지 입니다.")
    private List<Long> seats;
}
