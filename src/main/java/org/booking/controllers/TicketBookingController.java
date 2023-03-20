package org.booking.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.booking.model.Event;
import org.booking.model.Ticket;
import org.booking.model.User;
import org.booking.services.EventService;
import org.booking.services.TicketService;
import org.booking.services.UserService;
import org.booking.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Validated
public class TicketBookingController {
    private final EventService eventService;
    private final UserService userService;
    private final TicketService ticketService;

    @Value("${server.port}")
    private String port;

    private static final Logger logger = LoggerFactory.getLogger(TicketBookingController.class);

    @GetMapping(path = "/events/{id}")
    public Event getEventById(@PathVariable long id) {
        logger.info("Get event id={}", id);
        return eventService.getEventById(id)
                .orElseThrow(() -> new NoSuchEventException("There is no such event with id: " + id));
    }

    @GetMapping(path = "/events/title/{title}")
    public List<Event> getEventsByTitle(@PathVariable String title, @RequestParam int pageSize, @RequestParam int pageNum) {
        logger.info("Get event by title={}. Page size={}, page num = {}", title, pageSize, pageNum);
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @SneakyThrows
    @GetMapping(path = "/events/date/{date}")
    public List<Event> getEventsForDay(@PathVariable String date, @RequestParam int pageSize, @RequestParam int pageNum) {
        logger.info("Get event for date={}. Page size={}, page num = {}", date, pageSize, pageNum);
        //"2023-02-22"
        Date dateObj = DateUtils.getDate(date);
        return eventService.getEventsForDay(dateObj, pageSize, pageNum);
    }

    @PostMapping(path = "/event")
    public Event createEvent(@RequestBody Event event) {
        logger.info("Create event: {}", event);
        return eventService.createEvent(event);
    }

    @PutMapping(path = "/updateEvent")
    public Event updateEvent(@RequestBody Event event) {
        logger.info("Update event: {}", event);
        return eventService.updateEvent(event);
    }

    @DeleteMapping(value = "/deleteEvent")
    public void deleteEvent(@RequestBody Event event) {
        logger.info("Delete event id={}", event.getId());
        eventService.deleteEvent(event.getId());
    }

    @GetMapping(path = "/users/{id}")
    public User getUserById(@PathVariable long id) {
        logger.info("Get user id={}", id);
        return userService.getUserById(id)
                .orElseThrow(() -> new NoSuchUserException("There is no such user with id: " + id));
    }

    @GetMapping(path = "/users/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        logger.info("Get user by email={}", email);
        return userService
                .getUserByEmail(email)
                .orElseThrow(() -> new NoSuchUserException("There is no such user with email: " + email));
    }

    @GetMapping(path = "/users/name/{name}")
    public List<User> getUsersByName(@PathVariable String name, @RequestParam int pageSize, @RequestParam int pageNum) {
        logger.info("Get users by name={}. Page size={}, page num = {}", name, pageSize, pageNum);
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @PostMapping(path = "/user")
    public User createUser(@RequestBody User user) {
        logger.info("Create user: {}", user);
        return userService.createUser(user);
    }

    @PutMapping(path = "/updateUser")
    public User updateUser(@RequestBody User user) {
        logger.info("Update user: {}", user);
        return userService.updateUser(user);
    }

    @DeleteMapping(value = "/deleteUser")
    public void deleteUser(@RequestBody User user) {
        logger.info("Delete user id={}", user.getId());
        userService.deleteUser(user.getId());
    }

    @GetMapping(path = "/ticket")
    public Ticket bookTicket(@RequestParam long userId, @RequestParam long eventId, @RequestParam int place, @RequestParam Ticket.Category category) {
        validateUserExists(userId);
        logger.info("Book ticket for user id={} eventId={}, place={}, category={}",
                userId, eventId, place, category);
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    private void validateUserExists(long userId) {
        logger.info("Validate user with id={} exists", userId);
        userService.getUserById(userId)
                .orElseThrow(() -> new NoSuchUserException("There is no such user with id: " + userId));
    }

    @PostMapping(path = "/tickets/user")
    public List<Ticket> getBookedTickets(@RequestBody User user, @RequestParam int pageSize, @RequestParam int pageNum) {
        logger.info("Get booked tickets for user={}. Page size={}, page num = {}", user, pageSize, pageNum);
        return ticketService
                .getBookedTickets(user, pageSize, pageNum)
                .stream()
                .sorted(Comparator.comparing(this::getEventDate).reversed())
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/tickets/event")
    public List<Ticket> getBookedTickets(@RequestBody Event event, @RequestParam int pageSize, @RequestParam int pageNum) {
        logger.info("Get booked tickets for event={}. Page size={}, page num = {}", event, pageSize, pageNum);
        return ticketService
                .getBookedTickets(event, pageSize, pageNum)
                .stream()
                .sorted(Comparator.comparing(this::getUserEmail))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/cancelTicket")
    public void cancelTicket(@RequestParam long ticketId) {
        logger.info("Cancel ticket id={}", ticketId);
        ticketService.cancelTicket(ticketId);
    }

    private Date getEventDate(Ticket ticket) {
        Optional<Event> event = eventService.getEventById(ticket.getEventId());
        return event.map(Event::getDate).orElse(new Date());
    }

    private String getUserEmail(Ticket ticket) {
        Optional<User> user = userService.getUserById(ticket.getUserId());
        return user.map(User::getEmail).orElse("");
    }

}
