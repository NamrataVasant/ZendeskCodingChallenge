package com.zendesk.ticket.viewer.handlers;

import com.zendesk.ticket.viewer.models.Ticket;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketsServiceTest {

    private TicketsService ticketsService;
    private NetworkService networkService;

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
        if (ticket != null) {
            assertEquals(ticket.getId(), 1);
            assertEquals(ticket.getStatus(), "open");
            assertEquals(ticket.getTitle(), "Sample ticket: Meet the ticket");
            assertFalse(ticket.getDescription().isEmpty());
            assertEquals(ticket.getAssigned_to(), 53610909);
        }
    }

    @Test
    void getAllTickets() {
        ticketsService = new TicketsService();
        List<Ticket> tickets = ticketsService.getAllTickets(0);
        if (tickets != null && tickets.size() > 0) {
            assertEquals(tickets.get(0).getId(), 1);
            assertEquals(tickets.get(0).getStatus(), "open");
            assertEquals(tickets.get(0).getTitle(), "Sample ticket: Meet the ticket");
            assertFalse(tickets.get(0).getDescription().isEmpty());
            assertEquals(tickets.get(0).getAssigned_to(), 53610909);
        }
    }

    @Test
    void nextPage() {
        ticketsService = new TicketsService();
        networkService = mock(NetworkService.class);
        try {
            when(networkService.zenDeskAPICall("")).thenReturn(new JSONObject("{}"));
            ticketsService.getAllTickets(0);
            assertEquals(ticketsService.getAllTickets(1).get(0).getId(), 26);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void PreviousPage() {
        ticketsService = new TicketsService();
        networkService = mock(NetworkService.class);
        try {
            when(networkService.zenDeskAPICall("")).thenReturn(new JSONObject("{}"));
            ticketsService.getAllTickets(0);
            ticketsService.getAllTickets(1);
            assertEquals(ticketsService.getAllTickets(-1).get(0).getId(), 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void NullPreviousPage() {
        ticketsService = new TicketsService();
        networkService = mock(NetworkService.class);
        try {
            when(networkService.zenDeskAPICall("")).thenReturn(new JSONObject("{}"));
            ticketsService.getAllTickets(0);
            assertTrue(ticketsService.getAllTickets(-1).isEmpty());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}