package com.sadowbass.outerpark.application.account.exception;

import com.sadowbass.outerpark.application.product.domain.Ticket;
import com.sadowbass.outerpark.application.product.domain.TicketStatus;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CannotCancelTicketException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "티켓을 취소할 수 없습니다.";
    private List<TicketCancelFailedReason> failedTickets;

    public CannotCancelTicketException(List<Ticket> unAvailableCancelTickets) {
        super(EXCEPTION_MESSAGE);
        this.failedTickets = unAvailableCancelTickets.stream()
                .map(TicketCancelFailedReason::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class TicketCancelFailedReason {

        private Long ticketId;
        private String reason;

        private static final String ALREADY_CANCELED_TICKET = "이미 취소된 티켓입니다.";
        private static final String TICKET_ALREADY_OVERDUE = "이미 기간이 지난 티켓입니다.";

        public TicketCancelFailedReason(Ticket ticket) {
            this.ticketId = ticket.getId();
            this.reason = createReason(ticket.getStatus());
        }

        private String createReason(TicketStatus ticketStatus) {
            switch (ticketStatus) {
                case CANCELED:
                    return ALREADY_CANCELED_TICKET;
                case DONE:
                    return TICKET_ALREADY_OVERDUE;
                default:
                    throw new IllegalArgumentException("취소 가능한 티켓을 취소 시도 하였습니다.");
            }
        }
    }
}
