package com.sadowbass.outerpark.application.myinfo.service

import com.sadowbass.outerpark.application.account.dto.LoginResult
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.application.myinfo.dto.MyInfo
import com.sadowbass.outerpark.application.myinfo.dto.MyTicket
import com.sadowbass.outerpark.infra.session.LoginManager
import com.sadowbass.outerpark.infra.session.exception.LoginRequiredException
import com.sadowbass.outerpark.infra.utils.Pagination
import spock.lang.Specification

import java.time.LocalDate

class MyInfoServiceTest extends Specification {

    LoginManager loginManager
    AccountRepository accountRepository
    MyInfoService myInfoService

    def setup() {
        accountRepository = Mock(AccountRepository.class)
        loginManager = Mock(LoginManager.class)
        myInfoService = new MyInfoService(loginManager, accountRepository)
    }

    def "내 정보 조회 성공"() {
        given:
        def loginResult = new LoginResult(1L, "test@gmail.com")
        loginManager.getMember() >> loginResult

        def myInfo = new MyInfo("test@gmail.com", "유승철", "감자", "01047497649")
        accountRepository.findMyInfoById(loginResult.id) >> myInfo

        when:
        def result = myInfoService.retrieveMyInfo()

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
        myInfoService.retrieveMyInfo()

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
        def reservations = myInfoService.retrieveMyReservations(LocalDate.now(), page)

        then:
        reservations.content.size() == 1
    }
}
