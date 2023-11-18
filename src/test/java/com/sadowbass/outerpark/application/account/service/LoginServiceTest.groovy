package com.sadowbass.outerpark.application.account.service


import com.sadowbass.outerpark.application.account.domain.Account
import com.sadowbass.outerpark.application.account.exception.InvalidLoginInformationException
import com.sadowbass.outerpark.application.account.exception.NoSuchAccountDataException
import com.sadowbass.outerpark.application.account.repository.AccountRepository
import com.sadowbass.outerpark.infra.utils.PasswordUtils
import com.sadowbass.outerpark.presentation.dto.account.LoginRequest
import spock.lang.Shared
import spock.lang.Specification

class LoginServiceTest extends Specification {

    AccountRepository accountRepository
    LoginService loginService

    @Shared
    LoginRequest loginRequest

    def setupSpec() {
        loginRequest = new LoginRequest()
        loginRequest.email = "test@gmail.com"
        loginRequest.password = "password"
    }

    def setup() {
        accountRepository = Mock(AccountRepository.class)
        loginService = new LoginService(accountRepository)
    }

    def "로그인 성공"() {
        when:
        loginService.login(loginRequest)

        then:
        accountRepository.findByEmail(loginRequest.email) >> {
            def account = new Account()
            account.email = "test@gmail.com"
            account.password = PasswordUtils.encode("password")

            return account
        }
    }

    def "로그인 실패. Email 없음"() {
        when:
        loginService.login(loginRequest)

        then:
        accountRepository.findByEmail(loginRequest.email) >> null
        thrown(NoSuchAccountDataException.class)
    }

    def "로그인 실패. PW 불일치"() {
        given:
        def testAccount = new Account()
        testAccount.email = loginRequest.email
        testAccount.password = "wrong password"

        when:
        loginService.login(loginRequest)

        then:
        accountRepository.findByEmail(loginRequest.email) >> testAccount
        thrown(InvalidLoginInformationException.class)
    }
}
