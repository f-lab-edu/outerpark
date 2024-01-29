package com.sadowbass.outerpark.application.reservation.service;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException;
import com.sadowbass.outerpark.application.account.exception.DifferentCancelRequestSizeException;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.domain.Ticket;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import com.sadowbass.outerpark.application.reservation.dto.PendingId;
import com.sadowbass.outerpark.application.reservation.exception.AlreadyPendingException;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.validation.ValidationUtils;
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest;
import com.sadowbass.outerpark.presentation.dto.reservation.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final LoginManager loginManager;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public PendingId pendingReservation(Long roundId, ReservationRequest reservationRequest) {
        ValidationUtils.validate(reservationRequest);
        List<Long> seats = reservationRequest.getSeats();

        List<RoundSeats> enableRoundSeats = productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(roundId, seats);
        canPending(seats, enableRoundSeats);

        LoginResult user = loginManager.getMember();
        String pendingId = UUID.randomUUID().toString();
        enableRoundSeats.forEach(roundSeats -> roundSeats.makeReservation(user.getId(), pendingId));

        productRepository.pendingRoundSeats(enableRoundSeats);

        return new PendingId(pendingId);
    }

    private void canPending(List<Long> seats, List<RoundSeats> enableRoundSeats) {
        if (enableRoundSeats.size() < seats.size()) {
            throw new AlreadyPendingException();
        }
    }

    @Transactional
    public void reservation(Long roundId, String pendingId) {
        LoginResult user = loginManager.getMember();
        List<RoundSeats> roundSeats = productRepository.findPendingRoundSeats(user.getId(), roundId, pendingId);

        List<Long> seats = roundSeats.stream().map(RoundSeats::getId).collect(Collectors.toList());
        productRepository.reserveRoundSeats(seats, user.getId());
        productRepository.createTickets(seats, user.getId(), roundId);
    }

    @Transactional
    public void cancelReservations(ReservationCancelRequest cancelRequest) {
        ValidationUtils.validate(cancelRequest);

        Long memberId = loginManager.getMember().getId();
        List<Long> ticketIds = cancelRequest.getTicketIds();

        List<Ticket> tickets = accountRepository.findAllTicketsByMemberIdAndTicketIds(memberId, ticketIds);
        validCancelable(tickets, ticketIds.size());

        accountRepository.cancelTickets(ticketIds, memberId);

        List<Long> roundSeatIds = getRoundSeatIds(tickets);
        productRepository.returnRoundSeatsToEnable(roundSeatIds, memberId);
    }

    private void validCancelable(List<Ticket> tickets, int requestCount) {
        if (tickets.size() != requestCount) {
            throw new DifferentCancelRequestSizeException();
        }

        List<Ticket> cantCancelTickets = getNonCancelableTickets(tickets);
        if (!cantCancelTickets.isEmpty()) {
            throw new CannotCancelTicketException(cantCancelTickets);
        }
    }

    private List<Ticket> getNonCancelableTickets(List<Ticket> tickets) {
        return tickets.stream()
                .filter(ticket -> !ticket.canCancel())
                .collect(Collectors.toList());
    }

    private List<Long> getRoundSeatIds(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getRoundSeatId).collect(Collectors.toList());
    }
}
