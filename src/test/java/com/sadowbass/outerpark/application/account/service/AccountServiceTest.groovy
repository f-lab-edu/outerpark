package com.sadowbass.outerpark.application.account.service

import com.sadowbass.outerpark.application.account.domain.Account
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceTest extends Specification {

    AccountRepository accountRepository
    AccountService accountService

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
        accountService = new AccountService(accountRepository)
    }

    def "회원가입"() {
        expect:
        accountService.signUp(signUpRequest)
    }

    def "회원가입 실패, ID 중복"() {
        when:
        accountService.signUp(signUpRequest)

        then:
        accountRepository.findByEmail(signUpRequest.email) >> new Account()
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
}
