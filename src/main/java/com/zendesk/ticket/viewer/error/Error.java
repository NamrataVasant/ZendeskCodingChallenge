package com.zendesk.ticket.viewer.error;

public enum Error {
    INVALID_INPUT_ERROR("Sorry! Your selection is not valid. Please enter a valid selection"),
    FAILED_TO_RETRIEVE_TICKETS("Oops!! Sorry could not successfully fetch your ticket(s)!"),
    INVALID_INPUT_FOR_TICKET_PAGE_DISPLAY("Invalid page command, type 'n' for next, 'p' for back and 'menu' for main menu:"),
    PARSING_ERROR("There was an issue with parsing ticket json object"),
    DATE_UPDATE_ERROR("ERROR: Last update date may not appear correctly."),
    TICKET_NOT_FOUND("Ticket not found"),
    PROPERTIES_FILE_ERROR("Error getting properties file "),
    NETWORK_ERROR("Network Error!"),
    NO_META_DATA("No meta data"),
    NO_MORE_PAGES("No more pages left"),
    TICKET_FETCH_ERROR("Error fetching ticket "),
    NO_DATA_FROM_API("No data from API");


    private final String errorMessage;

    Error(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
