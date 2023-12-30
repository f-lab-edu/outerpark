package com.sadowbass.outerpark.application.product.service;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.product.domain.RoundSeats;
import com.sadowbass.outerpark.application.product.dto.AvailableSeat;
import com.sadowbass.outerpark.application.product.dto.ProductInfo;
import com.sadowbass.outerpark.application.product.dto.RoundInfo;
import com.sadowbass.outerpark.application.product.exception.AlreadyPendingException;
import com.sadowbass.outerpark.application.product.exception.NoSuchProductException;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.validation.ValidationUtils;
import com.sadowbass.outerpark.presentation.dto.product.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public String pending(Long roundId, ReservationRequest reservationRequest) {
        ValidationUtils.validate(reservationRequest);
        List<Long> seats = reservationRequest.getSeats();

        List<RoundSeats> enableRoundSeats = productRepository.findEnabledRoundSeatsByRoundIdAndSeatIds(roundId, seats);
        canPending(seats, enableRoundSeats);

        LoginResult user = loginManager.getMember();
        String pendingId = UUID.randomUUID().toString();
        enableRoundSeats.forEach(roundSeats -> roundSeats.makeReservation(user.getId(), pendingId));

        productRepository.pendingRoundSeats(enableRoundSeats);

        return pendingId;
    }

    private void canPending(List<Long> seats, List<RoundSeats> enableRoundSeats) {
        if (enableRoundSeats.size() < seats.size()) {
            throw new AlreadyPendingException();
        }
    }

    @Transactional
    public void reservation(Long roundId, String pendingId) {
        LoginResult user = loginManager.getMember();
        List<RoundSeats> roundSeats = productRepository.findPendingRoundSeats(user.getId(), roundId, pendingId);

        List<Long> seats = roundSeats.stream().map(RoundSeats::getId).collect(Collectors.toList());
        productRepository.reserveRoundSeats(seats, user.getId());
        productRepository.createTickets(seats, user.getId(), roundId);
    }
}