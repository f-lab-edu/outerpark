package com.sadowbass.outerpark.application.account.service

import com.sadowbass.outerpark.application.account.domain.Account
import com.sadowbass.outerpark.application.account.dto.AccountInfo
import com.sadowbass.outerpark.application.account.dto.LoginResult
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.infra.session.LoginManager
import com.sadowbass.outerpark.infra.session.exception.LoginRequiredException
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceTest extends Specification {

    AccountRepository accountRepository
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
        loginManager = Mock(LoginManager.class)
        accountService = new AccountService(accountRepository, loginManager)
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

        def accountInfo = new AccountInfo("test@gmail.com", "유승철", "감자", "01047497649")
        accountRepository.findAccountInfoById(loginResult.id) >> accountInfo

        when:
        def myInfo = accountService.retrieveMyInfo()

        then:
        myInfo.email == accountInfo.email
        myInfo.name == accountInfo.name
        myInfo.nickname == accountInfo.nickname
        myInfo.phone == accountInfo.phone
    }

    def "내 정보 조회 실패, 로그인이 되어 있지 않음"() {
        given:
        loginManager.getMember() >> { throw new LoginRequiredException() }

        def accountInfo = new AccountInfo("test@gmail.com", "유승철", "감자", "01047497649")
        accountRepository.findAccountInfoById(1L) >> accountInfo

        when:
        def myInfo = accountService.retrieveMyInfo()

        then:
        thrown(LoginRequiredException.class)
    }
}
