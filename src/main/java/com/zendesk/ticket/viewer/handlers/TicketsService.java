package com.zendesk.ticket.viewer.handlers;

import com.zendesk.ticket.viewer.models.Ticket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zendesk.ticket.viewer.error.Error.*;

public class TicketsService {

    // Used Paginating through lists using cursor pagination
    private static final int PAGE_LIMIT = 25;
    private final NetworkService networkService;
    String after_cursor;
    String before_cursor;
    boolean has_more;
    Scanner input;

    public TicketsService() {
        //Initialise the network handler class
        networkService = new NetworkService();
        input = new Scanner(System.in);

    }

    public List<Ticket> getAllTickets(int page) {

        //First time call for "Paginating through lists using cursor pagination"
        String resourcePath = "";
        switch (page) {
            case 1:                   // Next page
                if (!has_more) {
                    System.out.println(NO_MORE_PAGES.getErrorMessage());
                } else {
                    resourcePath = "tickets.json?page[size]=" + PAGE_LIMIT + "&page[after]=" + after_cursor;
                }
                break;
            case -1: // Previous page
                resourcePath = "tickets.json?page[size]=" + PAGE_LIMIT + "&page[before]=" + before_cursor;
                break;
            default: // First page without cursor
                resourcePath = "tickets.json?page[size]=" + PAGE_LIMIT;
        }

        //get tickets JSON from API
        JSONObject ticketsJSON = networkService.zenDeskAPICall(resourcePath);
        if (ticketsJSON == null) {
            return null;
        }

        List<Ticket> tickets = ParseJSONtoMultipleTickets(ticketsJSON);
        if (tickets == null) {
            System.out.println(NO_DATA_FROM_API.getErrorMessage());
            return null;
        }

        try {
            //get meta data for pagination
            JSONObject meta = ticketsJSON.getJSONObject("meta");
            has_more = meta.getBoolean("has_more");
            after_cursor = meta.getString("after_cursor");
            before_cursor = meta.getString("before_cursor");
        } catch (Exception e) {
            System.out.println(NO_META_DATA.getErrorMessage());
            return null;
        }

        displayTickets(tickets);
        return tickets;
    }

    public Ticket getSingleTicket(String ticketId) {
        String resourcePath = "tickets/" + ticketId + ".json";
        try {
            JSONObject ticketsData = (networkService.zenDeskAPICall(resourcePath)); //get the ticket
            if (ticketsData == null) {
                return null;
            }

            Ticket ticket = ParseJSONtoSingleTicket(ticketsData.getJSONObject("ticket"));

            System.out.println(ticket);
            return ticket;
        } catch (Exception e) {
            System.out.println(TICKET_FETCH_ERROR.getErrorMessage() + ticketId);
            e.printStackTrace();
            return null;
        }
    }


    private List<Ticket> ParseJSONtoMultipleTickets(JSONObject ticketsJSON) {
        try {
            JSONArray ticketsArray = ticketsJSON.getJSONArray("tickets");
            List<Ticket> ticketsList = new ArrayList<>();

            for (int i = 0; i < ticketsArray.length(); i++) {
                Ticket ticket = ParseJSONtoSingleTicket(ticketsArray.getJSONObject(i));
                if (ticket == null) {
                    System.out.println(TICKET_FETCH_ERROR.getErrorMessage() + i);
                }
                ticketsList.add(ticket);
            }
            return ticketsList;
        } catch (Exception ex) {
            System.out.println(PARSING_ERROR.getErrorMessage());
            return null;
        }
    }

    public Ticket ParseJSONtoSingleTicket(JSONObject ticketJSON) {
        try {
            //format the tickets into a ticket object
            Ticket ticket = new Ticket();
            ticket.setId(ticketJSON.getInt("id"));
            ticket.setDescription(ticketJSON.getString("description"));
            ticket.setStatus(ticketJSON.getString("status"));
            ticket.setTitle(ticketJSON.getString("subject"));
            try {
                ticket.setAssigned_to(ticketJSON.getInt("assignee_id"));
            } catch (JSONException e) {
                ticket.setAssigned_to(0);
            }
            //format the date of the ticket for display
            try {
                DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                Date date = DATE_FORMAT.parse(ticketJSON.getString("updated_at"));
                ticket.setUpdated_on(date);
            } catch (ParseException e) {
                System.out.println(DATE_UPDATE_ERROR.getErrorMessage());
                ticket.setUpdated_on(null);
            }
            ticket.setCreate_by(ticketJSON.getInt("requester_id"));
            return ticket;
        } catch (Exception ex) {
            System.out.println(TICKET_NOT_FOUND.getErrorMessage());
            return null;
        }
    }

    private void displayTickets(List<Ticket> tickets) {

        if (tickets.isEmpty()) {
            System.out.println(NO_DATA_FROM_API.getErrorMessage());
        } else {
            for (Ticket ticket : tickets) {

                String ret = "\n-------------\n Ticket: ";
                if (ticket.getId() != 0) {
                    ret += ticket.getId();
                } else {
                    ret += "Not available";
                }
                ret += " [" + ticket.getStatus() + "] " +
                        " " + ticket.getTitle();
                if (ticket.getAssigned_to() == 0) {
                    ret += "\n\t Assigned to: Not Assigned";
                } else {
                    ret += "\n\t Assigned to: " + ticket.getAssigned_to();
                }
                if (ticket.getCreate_by() != 0) {
                    ret += "\n\t Created by: " + ticket.getCreate_by();
                } else {
                    ret += "\n\t Created by: Not Available";
                }
                if (ticket.getUpdated_on() != null)
                    ret += "\n\t updated on: " + ticket.getUpdated_on();

                ret += "\n==============\n";
                System.out.println(ret);
            }
        }

    }
}

