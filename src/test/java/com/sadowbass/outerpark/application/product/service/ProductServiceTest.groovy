package com.sadowbass.outerpark.application.product.service


import com.sadowbass.outerpark.application.product.dto.ProductInfo
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException
import com.sadowbass.outerpark.application.product.repository.ProductRepository
import spock.lang.Specification

class ProductServiceTest extends Specification {

    def "공연 조회 성공"() {
        given:
        ProductRepository productRepository = Mock(ProductRepository.class);
        def productService = new ProductService(productRepository)
        def productId = 1L

        when:
        ProductInfo productInfo = productService.findProductInfoByProductId(productId)

        then:
        productRepository.findProductInfoByProductId(productId) >> {
            def product = new ProductInfo()
            product.id = 1L
            product.hallName = "올림픽공원"
            product.name = "드림씨어터 Live in Korea 2023"
            product.describe = "드림씨어터 내한공연"
            product.runningTime = 120

            return product
        }
    }

    def "공연 조회 실패, 존재하지 않는 productId"() {
        given:
        ProductRepository productRepository = Mock(ProductRepository.class);
        def productService = new ProductService(productRepository)
        def productId = 2L

        when:
        ProductInfo productInfo = productService.findProductInfoByProductId(productId)

        then:
        productRepository.findProductInfoByProductId(productId) >> null
        thrown(NoSuchProductException.class)
    }
}
