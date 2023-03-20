package org.booking.services.impl;

import lombok.AllArgsConstructor;
import org.booking.model.Event;
import org.booking.repository.EventRepository;
import org.booking.services.EventService;
import org.booking.utils.ListPaginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository repository;
    @Override
    public Optional getEventById(long eventId) {
        return repository.findById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Predicate<Event> eventPredicate = e -> e.getTitle().equalsIgnoreCase(title);
        return getFilteredEvents(eventPredicate, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Predicate<Event> datePredicate = e -> e.getDate().equals(day);
        return getFilteredEvents(datePredicate, pageSize, pageNum);
    }

    private List<Event> getFilteredEvents(Predicate<Event> predicate, int pageSize, int pageNum) {
        List<Event> sourceList = getAllEvents().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        return (List<Event>) ListPaginator.getPageList(sourceList, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        repository.save(event);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        return (Event) repository.save(event);
    }

    @Override
    public void deleteEvent(long eventId) {
        repository.deleteById(eventId);
    }

    private List<Event> getAllEvents() {
        return (List<Event>) repository.findAll();
    }

}
