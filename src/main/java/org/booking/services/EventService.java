package org.booking.services;

import org.booking.model.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventService {

    /**
     * Gets event by its id.
     *
     * @param eventId Event id.
     * @return Event.
     */
    Optional<Event> getEventById(long eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    void deleteEvent(long eventId);
}
