package org.booking.services.impl;

import lombok.AllArgsConstructor;
import org.booking.model.Event;
import org.booking.model.Ticket;
import org.booking.model.User;
import org.booking.repository.TicketRepository;
import org.booking.services.TicketService;
import org.booking.utils.ListPaginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private TicketRepository repository;

    private JmsTemplate jmsTemplate;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        validatePlaceFree(eventId, place);
        long id = getAllTickets().size() + 1;
        Ticket ticket = new Ticket(id, eventId, userId, category, place);
        jmsTemplate.convertAndSend("BookingTransactionQueue", ticket);
        return ticket;
    }

    private void validatePlaceFree(long eventId, int place) {
        long places = getAllTickets()
                .stream()
                .filter(e -> e.getEventId() == eventId)
                .filter(e -> e.getPlace() == place)
                .count();
        if (places > 0) {
            throw new OccupiedPlaceException("Place " + place + " for event " + eventId + " is already occupied");
        }
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Predicate<Ticket> ticketPredicate = t -> t.getUserId() == user.getId();
        return getFilteredTickets(ticketPredicate, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Predicate<Ticket> ticketPredicate = t -> t.getEventId() == event.getId();
        return getFilteredTickets(ticketPredicate, pageSize, pageNum);
    }

    @Override
    public void cancelTicket(long ticketId) {
        repository.deleteById(ticketId);
    }

    private List<Ticket> getAllTickets() {
        return (List<Ticket>) repository.findAll();
    }

    private List<Ticket> getFilteredTickets(Predicate<Ticket> ticketPredicate, int pageSize, int pageNum) {
        List<Ticket> sourceList = getAllTickets().stream()
                .filter(ticketPredicate)
                .collect(Collectors.toList());
        return (List<Ticket>) ListPaginator.getPageList(sourceList, pageSize, pageNum);
    }

}
