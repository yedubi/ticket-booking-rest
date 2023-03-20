package org.booking.receivers;

import lombok.AllArgsConstructor;
import org.booking.controllers.TicketBookingController;
import org.booking.model.Ticket;
import org.booking.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@AllArgsConstructor
public class BookingTransactionReceiver {
    private TicketRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(BookingTransactionReceiver.class);

    @JmsListener(destination = "${emp.jms.topic}", containerFactory = "empJmsContFactory")
    public void receiveMessage(Ticket ticket) {
        logger.info("Received <" + ticket + ">");
        repository.save(ticket);
    }

}
