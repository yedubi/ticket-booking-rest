package org.booking.cucumberglue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.booking.model.Ticket;
import org.booking.repository.TicketRepository;
import org.booking.services.impl.TicketServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;

import static org.booking.model.Ticket.Category.STANDARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@CucumberContextConfiguration
public class SpringIntegrationTest {
    private long userId;
    private long eventId;
    private int place;
    private Ticket.Category category;
    private Ticket ticket;
    @InjectMocks
    private TicketServiceImpl ticketBookingService;

    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private TicketRepository repository;

    @Given("^Empty repository")
    public void empty_repository() {
        given(repository.findAll()).willReturn(new ArrayList<>());
    }

    @Given("^I want to book ticket for user (.*) for event (.*) with place (.*) and category (.*)")
    public void ticket_parameters(long userId, long eventId, int place, Ticket.Category category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    @When("^I book ticket")
    public void book_ticket() {
        this.ticket = ticketBookingService.bookTicket(1L, 1L, 1, STANDARD);
    }

    @Then("^Validate booked ticket fields")
    public void validate_ticket_fields() {
        assertEquals(category, ticket.getCategory());
        assertEquals(place, ticket.getPlace());
        assertEquals(userId, ticket.getUserId());
        assertEquals(eventId, ticket.getEventId());
    }

    @Then("^Validate used correct jms topic")
    public void validate_jms_topic() {
        verify(jmsTemplate, times(1)).convertAndSend("BookingTransactionQueue", ticket);
    }

}
