package com.sadowbass.outerpark.learningtest.bcrypt

import at.favre.lib.crypto.bcrypt.BCrypt
import spock.lang.Specification

class BcryptTest extends Specification {

    def "bcrypt library 테스트"() {
        given:
        def originalPassword1 = "1234";
        def originalPassword2 = "string";
        def originalPassword3 = "combine123@!";

        when:
        def hash1 = BCrypt.withDefaults().hashToString(10, originalPassword1.toCharArray())
        def hash2 = BCrypt.withDefaults().hashToString(10, originalPassword2.toCharArray())
        def hash3 = BCrypt.withDefaults().hashToString(10, originalPassword3.toCharArray())

        then:
        BCrypt.verifyer().verify(originalPassword1.toCharArray(), hash1.toCharArray()).verified == true
        BCrypt.verifyer().verify(originalPassword2.toCharArray(), hash2.toCharArray()).verified == true
        BCrypt.verifyer().verify(originalPassword3.toCharArray(), hash3.toCharArray()).verified == true
        BCrypt.verifyer().verify(originalPassword1.toCharArray(), hash3.toCharArray()).verified == false
    }
}
