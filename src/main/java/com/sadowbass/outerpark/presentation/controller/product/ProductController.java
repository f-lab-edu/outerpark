package com.sadowbass.outerpark.presentation.controller.product;

import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.service.ProductService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public BaseResponse<ProductInfo> productInfo(@PathVariable Long productId) {
        ProductInfo productInfo = productService.findProductInfoByProductId(productId);
        return BaseResponse.okWithResult(productInfo);
    }

    @ExceptionHandler(NoSuchProductException.class)
    public BaseResponse<Void> handleNoSuchProductException(NoSuchProductException exception) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
    }
}
