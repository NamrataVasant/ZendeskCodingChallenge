package com.zendesk.ticket.viewer.handlers;

import com.zendesk.ticket.viewer.models.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketsServiceTest {

    private TicketsService ticketsService;

    @Test
    void getSingleInvalidTicket() {
        ticketsService = new TicketsService();
        Ticket ticket = ticketsService.getSingleTicket("123");
        assertNull(ticket);
    }

    @Test
    void getSingleTicket() {
        ticketsService = new TicketsService();
        Ticket ticket = ticketsService.getSingleTicket("1");

        assertNotNull(ticket);
        assertNotNull(ticket.getStatus());
        assertNotNull(ticket.getTitle());
        assertNotEquals(ticket.getDescription().isEmpty(), "");
    }

    @Test
    void getAllTickets() {
        ticketsService = new TicketsService();
        List<Ticket> tickets = ticketsService.getAllTickets(0);
        assertNotNull(tickets.get(0));
        assertNotEquals(tickets.get(0).getStatus().isEmpty(), "");
        assertNotEquals(tickets.get(0).getTitle().isEmpty(), "");
        assertNotEquals(tickets.get(0).getDescription().isEmpty(), "");
    }

    @Test
    void nextPage() {
        ticketsService = new TicketsService();
        ticketsService.getAllTickets(0);
        assertEquals(ticketsService.getAllTickets(1).get(0).getId(), 26);
    }

    @Test
    void PreviousPage() {
        ticketsService = new TicketsService();
        ticketsService.getAllTickets(0);
        ticketsService.getAllTickets(1);
        assertEquals(ticketsService.getAllTickets(-1).get(0).getId(), 1);
    }

    @Test
    void NullPreviousPage() {
        ticketsService = new TicketsService();
        ticketsService.getAllTickets(0);
        assertEquals(ticketsService.getAllTickets(-1).isEmpty(), true);
    }
}