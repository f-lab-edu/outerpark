package com.sadowbass.outerpark.application.reservation.service

import com.sadowbass.outerpark.application.account.dto.LoginResult
import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException
import com.sadowbass.outerpark.application.account.exception.DifferentCancelRequestSizeException
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.application.product.domain.RoundSeats
import com.sadowbass.outerpark.application.product.domain.Ticket
import com.sadowbass.outerpark.application.product.domain.TicketStatus
import com.sadowbass.outerpark.application.product.repository.ProductRepository
import com.sadowbass.outerpark.application.reservation.exception.AlreadyExpiredException
import com.sadowbass.outerpark.application.reservation.exception.AlreadyPendingException
import com.sadowbass.outerpark.infra.session.LoginManager
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest
import com.sadowbass.outerpark.presentation.dto.reservation.ReservationRequest
import spock.lang.Specification

import java.time.LocalDateTime

class ReservationServiceTest extends Specification {

    AccountRepository accountRepository
    ProductRepository productRepository;
    ReservationService reservationService;
    LoginManager loginManager
    LoginResult loginResult

    def setup() {
        accountRepository = Mock(AccountRepository.class)
        productRepository = Mock(ProductRepository.class)
        loginManager = Mock(LoginManager.class)
        loginResult = new LoginResult(1, "test@gmail.com")
        reservationService = new ReservationService(loginManager, productRepository, accountRepository)
    }

    def "공연 예매 성공"() {
        given:
        List<Long> list = List.of(1L, 2L, 3L, 4L)
        def request = new ReservationRequest()
        request.setSeats(list)

        productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(1, list) >> {
            def rs1 = new RoundSeats()
            def rs2 = new RoundSeats()
            def rs3 = new RoundSeats()
            def rs4 = new RoundSeats()

            def result = new ArrayList<>()
            result.add(rs1)
            result.add(rs2)
            result.add(rs3)
            result.add(rs4)

            return result
        }
        loginManager.getMember() >> loginResult

        when:
        def pendingId = reservationService.pendingReservation(1, request)

        then:
        noExceptionThrown()
        pendingId != null
        UUID.fromString(pendingId.pendingId)
    }

    def "공연 예매 실패, 좌석 미선택"() {
        given:
        List<Long> list = Collections.emptyList()
        def request = new ReservationRequest()
        request.setSeats(list)

        when:
        reservationService.pendingReservation(1, request)

        then:
        thrown(ValidationException.class)
    }

    def "공연 예매 실패, 좌석 초과 선택"() {
        given:
        List<Long> list = List.of(1L, 2L, 3L, 4L, 5L)
        def request = new ReservationRequest()
        request.setSeats(list)

        when:
        reservationService.pendingReservation(1, request)

        then:
        thrown(ValidationException.class)
    }

    def "공연 예매 실패, 이미 선택된 좌석"() {
        given:
        List<Long> list = List.of(1L, 2L, 3L, 4L)
        def request = new ReservationRequest()
        request.setSeats(list)

        productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(1, list) >> {
            def rs1 = new RoundSeats()
            def rs2 = new RoundSeats()
            def rs3 = new RoundSeats()

            def result = new ArrayList<>()
            result.add(rs1)
            result.add(rs2)
            result.add(rs3)

            return result
        }

        when:
        reservationService.pendingReservation(1, request)

        then:
        thrown(AlreadyPendingException.class)
    }

    def "공연 최종 예매 성공"() {
        given:
        loginManager.getMember() >> loginResult

        def roundId = 1
        def pendingId = UUID.randomUUID().toString()

        def expire = LocalDateTime.of(2025, 1, 1, 12, 0, 0)
        def seats1 = new RoundSeats()
        seats1.expire = expire
        def seats2 = new RoundSeats()
        seats2.expire = expire

        def roundSeats = Arrays.asList(seats1, seats2);
        productRepository.findPendingRoundSeats(loginResult.getId(), roundId, pendingId) >> roundSeats

        when:
        reservationService.reservation(roundId, pendingId)

        then:
        noExceptionThrown()
    }

    def "공연 최종 예매 실패, 결제기한 초과"() {
        given:
        loginManager.getMember() >> loginResult

        def roundId = 1
        def pendingId = UUID.randomUUID().toString()

        def expire = LocalDateTime.of(2023, 1, 1, 12, 0, 0)
        def seats1 = new RoundSeats()
        seats1.expire = expire
        def seats2 = new RoundSeats()
        seats2.expire = expire

        def roundSeats = Arrays.asList(seats1, seats2);
        productRepository.findPendingRoundSeats(loginResult.getId(), roundId, pendingId) >> roundSeats

        when:
        reservationService.reservation(roundId, pendingId)

        then:
        thrown(AlreadyExpiredException.class)
    }

    def "예약 취소 성공"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def cancelRequest = new ReservationCancelRequest()
        cancelRequest.ticketIds = List.of(1L)

        def ticket1 = new Ticket();
        ticket1.id = 1L;
        ticket1.status = TicketStatus.RESERVED

        def tickets = List.of(ticket1)

        accountRepository.findAllTicketsByMemberIdAndTicketIds(loginResult.id, cancelRequest.ticketIds) >> tickets

        when:
        reservationService.cancelReservations(cancelRequest)

        then:
        1 == 1
    }

    def "예약 취소 실패. 취소 요청 티켓수와 조회 된 티켓 수가 다름"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def cancelRequest = new ReservationCancelRequest()
        cancelRequest.ticketIds = List.of(1L)

        accountRepository.findAllTicketsByMemberIdAndTicketIds(loginResult.id, cancelRequest.ticketIds) >> Collections.emptyList()

        when:
        reservationService.cancelReservations(cancelRequest)

        then:
        thrown(DifferentCancelRequestSizeException.class)
    }

    def "예약 취소 실패. 이미 취소된 티켓 혹은 기간 만료"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def cancelRequest = new ReservationCancelRequest()
        cancelRequest.ticketIds = List.of(1L, 2L)

        def ticket1 = new Ticket();
        ticket1.id = 1L;
        ticket1.status = TicketStatus.CANCELED

        def ticket2 = new Ticket();
        ticket2.id = 1L;
        ticket2.status = TicketStatus.DONE

        def tickets = List.of(ticket1, ticket2)

        accountRepository.findAllTicketsByMemberIdAndTicketIds(loginResult.id, cancelRequest.ticketIds) >> tickets

        when:
        reservationService.cancelReservations(cancelRequest)

        then:
        def exception = thrown(CannotCancelTicketException.class)
        def failedTickets = exception.failedTickets

        failedTickets.size() == 2
    }
}
