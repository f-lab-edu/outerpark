package com.sadowbass.outerpark.application.product.service

import com.sadowbass.outerpark.application.account.dto.LoginResult
import com.sadowbass.outerpark.application.product.dto.ProductInfo
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException
import com.sadowbass.outerpark.application.product.repository.ProductRepository
import spock.lang.Specification

class ProductServiceTest extends Specification {

    ProductRepository productRepository
    ProductService productService
    LoginResult loginResult

    def setup() {
        productRepository = Mock(ProductRepository.class)
        productService = new ProductService(productRepository)
        loginResult = new LoginResult(1, "test@gmail.com")
    }

    def "공연 조회 성공"() {
        given:
        def productId = 1L

        productRepository.findProductInfoByProductId(productId) >> {
            def product = new ProductInfo()
            product.id = 1L
            product.hallName = "올림픽공원"
            product.name = "드림씨어터 Live in Korea 2023"
            product.describe = "드림씨어터 내한공연"
            product.runningTime = 120

            return product
        }

        when:
        ProductInfo productInfo = productService.findProductInfoByProductId(productId)

        then:
        productInfo != null
    }

    def "공연 조회 실패, 존재하지 않는 productId"() {
        given:
        def productId = 2L

        when:
        productService.findProductInfoByProductId(productId)

        then:
        productRepository.findProductInfoByProductId(productId) >> null
        thrown(NoSuchProductException.class)
    }
}
