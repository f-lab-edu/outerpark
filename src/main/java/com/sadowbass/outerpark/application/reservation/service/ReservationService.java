package com.sadowbass.outerpark.application.reservation.service;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException;
import com.sadowbass.outerpark.application.account.exception.DifferentCancelRequestSizeException;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.domain.Ticket;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import com.sadowbass.outerpark.application.reservation.dto.PendingId;
import com.sadowbass.outerpark.application.reservation.exception.AlreadyExpiredException;
import com.sadowbass.outerpark.application.reservation.exception.AlreadyPendingException;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.validation.ValidationUtils;
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest;
import com.sadowbass.outerpark.presentation.dto.reservation.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final LoginManager loginManager;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    // 1차개선
    // 1. Round / Seat / Grade 조인
    // 2. 조인 역정규화 > 성능측정 (query explain)

    // select for update 대신 분산락을 쓰겠다
    // 성능 테스트 해보면
    // select for update 가 훨씬 좋을듯.
    // Redis 로의 RTT 생김. lock 은 어차피 둘다 사용

    // 2차개선 ?
    // RDB 부하를 줄이는 것
    // 1. Pending 데이터 구조를 없앰 > Redis 로 이전
    // 2. 공연 전체에 대한 matrix 는 회차가 마무리 되는 이후(공연마감 ?) 별도의 DB 에 저장
    // ?
    @Transactional
    public PendingId pendingReservation(Long roundId, ReservationRequest reservationRequest) {
        ValidationUtils.validate(reservationRequest);
        // redis 캐시에 예약 가능한 좌석인지 확인

        List<Long> seats = reservationRequest.getSeats();

        List<RoundSeats> enableRoundSeats = productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(roundId, seats);
        canPending(seats, enableRoundSeats);

        LoginResult user = loginManager.getMember();
        String pendingId = UUID.randomUUID().toString();
        enableRoundSeats.forEach(roundSeats -> roundSeats.makeReservation(user.getId(), pendingId));
        // redis 캐시에서 내가 예약한 좌석 insert (ttl 5분)

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

        validExpire(roundSeats, user.getId());

        List<Long> seats = roundSeats.stream().map(RoundSeats::getId).collect(Collectors.toList());

        productRepository.reserveRoundSeats(seats, user.getId());
        productRepository.createTickets(seats, user.getId(), roundId);
    }

    private void validExpire(List<RoundSeats> roundSeats, Long memberId) {
        LocalDateTime now = LocalDateTime.now();

        boolean isExpired = roundSeats.stream()
                .anyMatch(rs -> isExpired(rs, now));

        if (isExpired) {
            handleExpiredReservation(roundSeats, memberId);
        }
    }

    private boolean isExpired(RoundSeats roundSeats, LocalDateTime now) {
        return now.isAfter(roundSeats.getExpire());
    }

    private void handleExpiredReservation(List<RoundSeats> expiredRoundSeats, Long memberId) {
        List<Long> roundSeatIds = expiredRoundSeats
                .stream()
                .map(RoundSeats::getId)
                .collect(Collectors.toList());

        productRepository.returnRoundSeatsToEnable(roundSeatIds, memberId);

        throw new AlreadyExpiredException();
    }

    @Transactional
    public void cancelReservations(ReservationCancelRequest cancelRequest) {
        ValidationUtils.validate(cancelRequest);

        Long memberId = loginManager.getMember().getId();
        List<Long> ticketIds = cancelRequest.getTicketIds();

        List<Ticket> tickets = accountRepository.findAllTicketsByMemberIdAndTicketIds(memberId, ticketIds);
        validCancelable(tickets, ticketIds.size());

        accountRepository.cancelTickets(ticketIds, memberId);

        List<Long> roundSeatIds = tickets
                .stream()
                .map(Ticket::getRoundSeatId)
                .collect(Collectors.toList());

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
}
