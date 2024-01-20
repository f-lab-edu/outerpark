package com.sadowbass.outerpark.application.account.service;

import com.sadowbass.outerpark.application.account.domain.Account;
import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.dto.MyInfo;
import com.sadowbass.outerpark.application.account.exception.CannotCancelTicketException;
import com.sadowbass.outerpark.application.account.exception.DifferentCancelRequestSizeException;
import com.sadowbass.outerpark.application.account.exception.DuplicateEmailException;
import com.sadowbass.outerpark.application.account.repository.AccountRepository;
import com.sadowbass.outerpark.application.product.domain.Ticket;
import com.sadowbass.outerpark.application.product.dto.MyTicket;
import com.sadowbass.outerpark.application.product.repository.ProductRepository;
import com.sadowbass.outerpark.infra.session.LoginManager;
import com.sadowbass.outerpark.infra.utils.Pagination;
import com.sadowbass.outerpark.infra.utils.PasswordUtils;
import com.sadowbass.outerpark.infra.utils.validation.ValidationUtils;
import com.sadowbass.outerpark.presentation.dto.PageResult;
import com.sadowbass.outerpark.presentation.dto.account.ReservationCancelRequest;
import com.sadowbass.outerpark.presentation.dto.account.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final LoginManager loginManager;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        ValidationUtils.validate(signUpRequest);

        Account exist = accountRepository.findByEmail(signUpRequest.getEmail());
        if (exist != null) {
            throw new DuplicateEmailException(signUpRequest.getEmail());
        }

        String encodedPassword = PasswordUtils.encode(signUpRequest.getPassword());
        Account account = Account.create(
                signUpRequest.getEmail(),
                encodedPassword,
                signUpRequest.getName(),
                signUpRequest.getNickname(),
                signUpRequest.getPhone()
        );

        accountRepository.save(account);

        account.addCreator(account.getId());
        accountRepository.updateCreateData(account);
    }

    public MyInfo retrieveMyInfo() {
        LoginResult member = loginManager.getMember();
        return accountRepository.findMyInfoById(member.getId());
    }

    public PageResult<MyTicket> retrieveMyReservations(LocalDate startDate, Pagination pagination) {
        LoginResult member = loginManager.getMember();
        int totalCount = accountRepository.findMyReservationsCount(member.getId(), startDate);
        List<MyTicket> myReservations = accountRepository.findMyReservations(member.getId(), startDate, pagination);

        return new PageResult<>(pagination, totalCount, myReservations);
    }

    @Transactional
    public void cancelReservations(ReservationCancelRequest cancelRequest) {
        ValidationUtils.validate(cancelRequest);

        Long memberId = loginManager.getMember().getId();
        List<Long> ticketIds = cancelRequest.getTicketIds();

        List<Ticket> tickets = accountRepository.findAllTicketsByMemberIdAndTicketIds(memberId, ticketIds);
        validCancelable(tickets, ticketIds.size());

        accountRepository.cancelTickets(ticketIds, memberId);

        List<Long> roundSeatIds = getRoundSeatIds(tickets);
        productRepository.returnRoundSeatsToEnable(roundSeatIds, memberId);
    }

    private void validCancelable(List<Ticket> tickets, int requestCount) {
        if (tickets.size() != requestCount) {
            throw new DifferentCancelRequestSizeException();
        }

        List<Ticket> cantCancelTickets = getNonCancelableTickets(tickets);
        if (!cantCancelTickets.isEmpty()) {
            throw new CannotCancelTicketException(cantCancelTickets);
        }
    }

    private List<Ticket> getNonCancelableTickets(List<Ticket> tickets) {
        return tickets.stream()
                .filter(ticket -> !ticket.canCancel())
                .collect(Collectors.toList());
    }

    private List<Long> getRoundSeatIds(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getRoundSeatId).collect(Collectors.toList());
    }
}
