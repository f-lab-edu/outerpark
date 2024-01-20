package com.sadowbass.outerpark.presentation.dto.account;

import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class ReservationCancelRequest {

    @Size(min = 1)
    private List<Long> ticketIds;
}
