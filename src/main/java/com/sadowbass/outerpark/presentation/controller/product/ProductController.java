package com.sadowbass.outerpark.presentation.controller.product;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.service.ProductService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.DetailResponse;
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
    public DetailResponse getProductInfo(@PathVariable Long productId) {
        ProductInfo productInfo = productService.findProductInfoByProductId(productId);
        return DetailResponse.ok(productInfo);
    }

    @GetMapping("/{productId}/rounds")
    public DetailResponse getProductRounds(@PathVariable Long productId) {
        List<RoundInfo> roundInfos = productService.findRoundInfosByProductId(productId);
        return DetailResponse.ok(roundInfos);
    }

    @GetMapping("/{productId}/rounds/{roundId}")
    public DetailResponse getAvailableSeats(@PathVariable Long productId, @PathVariable Long roundId, @RequestParam("seatType") Long gradeId) {
        List<AvailableSeat> availableSeats = productService.findAvailableSeatsByRoundIdAndGradeId(roundId, gradeId);
        return DetailResponse.ok(availableSeats);
    }

    @ExceptionHandler(NoSuchProductException.class)
    public BaseResponse handleNoSuchProductException(NoSuchProductException exception) {
        return new BaseResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
