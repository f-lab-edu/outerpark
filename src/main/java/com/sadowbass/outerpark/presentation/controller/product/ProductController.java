package com.sadowbass.outerpark.presentation.controller.product;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.service.ProductService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public BaseResponse<ProductInfo> getProductInfo(@PathVariable Long productId) {
        ProductInfo productInfo = productService.findProductInfoByProductId(productId);
        return BaseResponse.okWithResult(productInfo);
    }

    @GetMapping("/{productId}/rounds")
    public BaseResponse<List<RoundInfo>> getRoundsInfo(@PathVariable Long productId) {
        List<RoundInfo> roundInfos = productService.findRoundInfosByProductId(productId);
        return BaseResponse.okWithResult(roundInfos);
    }

    @GetMapping("/{productId}/rounds/{roundId}")
    public BaseResponse<List<AvailableSeat>> getAvailableSeats(@PathVariable Long roundId, @RequestParam("gradeId") Long gradeId) {
        List<AvailableSeat> availableSeats = productService.findAvailableSeatsByRoundIdAndGradeId(roundId, gradeId);
        return BaseResponse.okWithResult(availableSeats);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchProductException.class)
    public BaseResponse<Void> handleNoSuchProductException(NoSuchProductException exception) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
    }
}
