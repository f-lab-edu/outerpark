package com.sadowbass.outerpark.application.product.service;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.validation.ValidationUtils;
import com.sadowbass.outerpark.presentation.dto.product.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final LoginManager loginManager;

    public ProductInfo findProductInfoByProductId(Long productId) {
        ProductInfo productInfo = productRepository.findProductInfoByProductId(productId);
        if (productInfo == null) {
            throw new NoSuchProductException();
        }

        return productInfo;
    }

    public List<RoundInfo> findRoundInfosByProductId(Long productId) {
        return productRepository.findRoundInfosByProductId(productId);
    }

    public List<AvailableSeat> findAvailableSeatsByRoundIdAndGradeId(Long roundId, Long gradeId) {
        return productRepository.findAvailableSeatByRoundIdAndGradeId(roundId, gradeId);
    }

    @Transactional
    public void reservation(Long roundId, ReservationRequest reservationRequest) {
        ValidationUtils.validate(reservationRequest);
        List<Long> seats = reservationRequest.getSeats();

        List<RoundSeats> enableRoundSeats = productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(roundId, seats);
        if (seats.size() != enableRoundSeats.size()) {
            //TODO create custom exceptions
            throw new RuntimeException("이미 선택된 좌석입니다.");
        }

        LoginResult user = loginManager.getUser();
        enableRoundSeats.forEach(roundSeats -> roundSeats.makeReservation(user.getId()));

        productRepository.updateRoundSeats(enableRoundSeats);
    }
}
