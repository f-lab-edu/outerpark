package com.sadowbass.outerpark.presentation.controller.product;

import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.AlreadyPendingException;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.service.ProductService;
import com.sadowbass.outerpark.presentation.dto.BaseResponse;
import com.sadowbass.outerpark.presentation.dto.product.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
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

    @PostMapping("/{productId}/rounds/{roundId}")
    public BaseResponse<String> pending(@PathVariable Long roundId, @RequestBody ReservationRequest reservationRequest) {
        String pendingId = productService.pending(roundId, reservationRequest);
        return BaseResponse.okWithResult(pendingId);
    }

    @PostMapping("/{productId}/rounds/{roundId}/pendings/{pendingId}")
    public BaseResponse<Void> reservation(@PathVariable Long roundId, @PathVariable String pendingId) {
        productService.reservation(roundId, pendingId);
        return BaseResponse.ok();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchProductException.class)
    public BaseResponse<Void> handleNoSuchProductException(NoSuchProductException exception) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CannotAcquireLockException.class, AlreadyPendingException.class})
    public BaseResponse<Void> handleLockException() {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), AlreadyPendingException.EXCEPTION_MESSAGE, null);
    }
}
