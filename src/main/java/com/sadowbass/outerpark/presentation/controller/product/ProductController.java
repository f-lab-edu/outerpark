package com.sadowbass.outerpark.presentation.controller.product;

import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.service.ProductService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.DetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public DetailResponse productInfo(@PathVariable Long productId) {
        ProductInfo productInfo = productService.findProductInfoByProductId(productId);
        return new DetailResponse(HttpStatus.OK.value(), null, productInfo);
    }

    @ExceptionHandler(NoSuchProductException.class)
    public BaseResponse handleNoSuchProductException(NoSuchProductException exception) {
        return new BaseResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
