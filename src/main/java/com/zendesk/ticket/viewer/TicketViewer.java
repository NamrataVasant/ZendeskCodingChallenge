package com.zendesk.ticket.viewer;

import com.zendesk.ticket.viewer.handlers.TicketsService;
import java.util.Scanner;

import static com.zendesk.ticket.viewer.error.Error.*;

public class TicketViewer {

  public static void main(String[] args) {

    String userResponse;

    Scanner input = new Scanner(System.in);
    TicketsService ticketsService = new TicketsService();

    displayWelcomeMessage();

    while (true) {
      displayMenu();

      userResponse = input.nextLine();
      switch (userResponse) {
        case "1":           //Show all tickets
          System.out.println("Fetching tickets, please wait");
          int paging = 0;
          do {
            //Get all tickets
            if (ticketsService.getAllTickets(paging) == null) {
              System.out.println(NO_DATA_FROM_API.getErrorMessage());
              break;
            }

            //Pagination
            System.out.println(
                "\nEnter 'n' for next page, 'p' for previous and 'm' to return to main menu: ");
            userResponse = input.nextLine();
            if (userResponse.equals("m")) {
              break; //return to menu
            } else if (userResponse.equals("n")) {
              paging = 1; //next page
            } else if (userResponse.equals("p")) {
              paging = -1; //next page
            }
          } while (true);
          break;
        case "2":
          System.out.println("Please enter the ticket id:");
          userResponse = input.nextLine();
          if (ticketsService.getSingleTicket(userResponse) == null) {
            System.out.println(NO_DATA_FROM_API.getErrorMessage());
          }
          break;
        case "q":
          System.out.println("Thank you for using the viewer. Goodbye");
          return;
        default:
          System.out.println(INVALID_INPUT_ERROR.getErrorMessage() + "\n");
      }
    }

  }

  private static void displayWelcomeMessage() {
    System.out.println("\n\tWelcome to zendesk ticket viewer!!\n");
  }

  public static void displayMenu() {
    System.out.println("-----------------------------------");
    System.out.println("\nMENU");
    System.out.println("Please select an options:");
    System.out.println("\t* Enter 1 to view all tickets");
    System.out.println("\t* Enter 2 to view a ticket");
    System.out.println("\t* Enter 'q' to exit");
    System.out.println("-----------------------------------");
  }
}
