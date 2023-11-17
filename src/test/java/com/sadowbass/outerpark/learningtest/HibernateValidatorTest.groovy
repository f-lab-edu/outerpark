package com.sadowbass.outerpark.learningtest

import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import javax.validation.Validator

@SpringBootTest
class HibernateValidatorTest extends Specification {

    @Autowired
    Validator validator

    @Autowired
    ApplicationContext applicationContext

    def "test autoconfiguration validator bean"() {
        expect:
        applicationContext.getBean(org.springframework.validation.Validator) != null
    }

    def "test correct email validation"() {
        given:
        SignUpRequest correctRequest = new SignUpRequest()
        correctRequest.setEmail("test@test.com")

        when:
        def correctValidate = validator.validate(correctRequest)

        then:
        correctValidate.size() == 0
    }

    def "test incorrect email validation"() {
        given:
        SignUpRequest inCorrectRequest = new SignUpRequest()
        inCorrectRequest.setEmail("test2test.com")

        when:
        def unCorrectValidate = validator.validate(inCorrectRequest)

        then:
        unCorrectValidate.size() == 1

        for (def each in unCorrectValidate) {
            println "each = $each"
        }
    }

}
