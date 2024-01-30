package com.sadowbass.outerpark.presentation.controller.reservation;

import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException;
import com.sadowbass.outerpark.application.reservation.dto.PendingId;
import com.sadowbass.outerpark.application.reservation.exception.AlreadyExpiredException;
import com.sadowbass.outerpark.application.reservation.exception.AlreadyPendingException;
import com.sadowbass.outerpark.application.reservation.service.ReservationService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest;
import com.sadowbass.outerpark.presentation.dto.reservation.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/products/{productId}/rounds/{roundId}")
    public BaseResponse<PendingId> pendingReservation(@PathVariable Long roundId, @RequestBody ReservationRequest reservationRequest) {
        PendingId pendingId = reservationService.pendingReservation(roundId, reservationRequest);
        return BaseResponse.okWithResult(pendingId);
    }

    @PostMapping("/products/{productId}/rounds/{roundId}/pendings/{pendingId}")
    public BaseResponse<Void> reservation(@PathVariable Long roundId, @PathVariable String pendingId) {
        reservationService.reservation(roundId, pendingId);
        return BaseResponse.ok();
    }

    @DeleteMapping("/me/reservations")
    public BaseResponse<Void> cancelReservations(@RequestBody ReservationCancelRequest cancelRequest) {
        reservationService.cancelReservations(cancelRequest);
        return BaseResponse.ok();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CannotCancelTicketException.class)
    public BaseResponse<List<CannotCancelTicketException.TicketCancelFailedReason>> handleCannonCancelTicketException(CannotCancelTicketException cannotCancelTicketException) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), cannotCancelTicketException.getMessage(), cannotCancelTicketException.getFailedTickets());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CannotAcquireLockException.class, AlreadyPendingException.class})
    public BaseResponse<Void> handleLockException() {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), AlreadyPendingException.EXCEPTION_MESSAGE, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExpiredException.class)
    public BaseResponse<Void> handleAlreadyExpiredException() {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), AlreadyExpiredException.EXCEPTION_MESSAGE, null);
    }
}
