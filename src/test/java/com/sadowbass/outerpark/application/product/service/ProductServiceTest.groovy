package com.sadowbass.outerpark.application.product.service


import com.sadowbass.outerpark.application.product.dto.ProductInfo
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException
import com.sadowbass.outerpark.application.product.repository.ProductRepository
import com.sadowbass.outerpark.infra.utils.validation.exception.ValidationException
import com.sadowbass.outerpark.presentation.dto.product.ReservationRequest
import spock.lang.Specification

class ProductServiceTest extends Specification {

    ProductRepository productRepository;
    ProductService productService;

    def setup() {
        productRepository = Mock(ProductRepository.class)
        productService = new ProductService(productRepository)
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
        ProductRepository productRepository = Mock(ProductRepository.class)
        def productService = new ProductService(productRepository)
        def productId = 2L

        when:
        productService.findProductInfoByProductId(productId)

        then:
        productRepository.findProductInfoByProductId(productId) >> null
        thrown(NoSuchProductException.class)
    }

    def "공연 예매 실패, 좌석 미선택"() {
        given:
        List<Long> list = Collections.emptyList();
        def request = new ReservationRequest()
        request.setSeats(list);

        when:
        productService.reservation(1, request)

        then:
        thrown(ValidationException.class)
    }

    def "공연 예매 실패, 좌석 초과 선택"() {
        given:
        List<Long> list = List.of(1L, 2L, 3L, 4L, 5L)
        def request = new ReservationRequest()
        request.setSeats(list);

        when:
        productService.reservation(1, request)

        then:
        thrown(ValidationException.class)
    }
}
