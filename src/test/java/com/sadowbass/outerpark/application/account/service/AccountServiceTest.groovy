package com.sadowbass.outerpark.application.account.service

import com.sadowbass.outerpark.application.account.domain.Account
import com.sadowbass.outerpark.application.account.dto.LoginResult
import com.sadowbass.outerpark.application.account.dto.MyInfo
import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException
import com.sadowbass.outerpark.application.account.exception.DifferentCancelRequestSizeException
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.application.product.domain.Ticket
import com.sadowbass.outerpark.application.product.domain.TicketStatus
import com.sadowbass.outerpark.application.product.dto.MyTicket
import com.sadowbass.outerpark.application.product.repository.ProductRepository
import com.sadowbass.outerpark.infra.session.LoginManager
import com.sadowbass.outerpark.infra.session.exception.LoginRequiredException
import com.sadowbass.outerpark.infra.utils.Pagination
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class AccountServiceTest extends Specification {

    AccountRepository accountRepository
    ProductRepository productRepository
    AccountService accountService
    LoginManager loginManager

    @Shared
    SignUpRequest signUpRequest

    def setupSpec() {
        signUpRequest = new SignUpRequest()
        signUpRequest.email = "shinwa46@gmail.com"
        signUpRequest.password = "password"
        signUpRequest.name = "유승철"
        signUpRequest.nickname = "sadowbass"
        signUpRequest.phone = "01011111111"
    }

    def setup() {
        accountRepository = Mock(AccountRepository.class)
        productRepository = Mock(ProductRepository.class)
        loginManager = Mock(LoginManager.class)
        accountService = new AccountService(accountRepository, productRepository, loginManager)
    }

    def "회원가입"() {
        expect:
        accountService.signUp(signUpRequest)
    }

    def "회원가입 실패, ID 중복"() {
        given:
        accountRepository.findByEmail(signUpRequest.email) >> new Account()

        when:
        accountService.signUp(signUpRequest)

        then:
        thrown(DuplicateEmailException.class)
    }

    def "회원가입 실패, email 형식 오류"() {
        given:
        def wrongRequest = new SignUpRequest()
        wrongRequest.email = "wrong2email.com"

        when:
        accountService.signUp(wrongRequest)

        then:
        thrown(ValidationException.class)
    }

    def "내 정보 조회 성공"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def myInfo = new MyInfo("test@gmail.com", "유승철", "감자", "01047497649")
        accountRepository.findMyInfoById(loginResult.id) >> myInfo

        when:
        def result = accountService.retrieveMyInfo()

        then:
        result.email == myInfo.email
        result.name == myInfo.name
        result.nickname == myInfo.nickname
        result.phone == myInfo.phone
    }

    def "내 정보 조회 실패, 로그인이 되어 있지 않음"() {
        given:
        loginManager.getMember() >> { throw new LoginRequiredException() }

        def myInfo = new MyInfo("test@gmail.com", "유승철", "감자", "01047497649")
        accountRepository.findMyInfoById(1L) >> myInfo

        when:
        accountService.retrieveMyInfo()

        then:
        thrown(LoginRequiredException.class)
    }

    def "내 예약 조회 성공"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        accountRepository.findMyReservationsCount(1L, LocalDate.now()) >> 1

        def page = new Pagination(10, 1)
        def myTicket = new MyTicket()
        myTicket.productName = "드림씨어터 Live in Korea"

        accountRepository.findMyReservations(1L, LocalDate.now(), page) >> Collections.singletonList(myTicket)

        when:
        def reservations = accountService.retrieveMyReservations(LocalDate.now(), page)

        then:
        reservations.content.size() == 1
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
        accountService.cancelReservations(cancelRequest)

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
        accountService.cancelReservations(cancelRequest)

        then:
        thrown(DifferentCancelRequestSizeException.class)
    }

    def "예약 취소 실패. 이미 취소된 티켓 혹은 기간 만료"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def cancelRequest = new ReservationCancelRequest()
        cancelRequest.ticketIds = List.of(1L)

        def ticket1 = new Ticket();
        ticket1.id = 1L;
        ticket1.status = TicketStatus.CANCELED

        def ticket2 = new Ticket();
        ticket2.id = 1L;
        ticket2.status = TicketStatus.DONE

        def tickets = List.of(ticket1, ticket2)

        accountRepository.findAllTicketsByMemberIdAndTicketIds(loginResult.id, cancelRequest.ticketIds) >> tickets

        when:
        accountService.cancelReservations(cancelRequest)

        then:
        def exception = thrown(CannotCancelTicketException.class)
        def failedTickets = exception.failedTickets

        failedTickets.size() == 2

    }
}
