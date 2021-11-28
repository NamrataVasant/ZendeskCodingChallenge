package com.zendesk.ticket.viewer.handlers;

import static com.zendesk.ticket.viewer.error.Error.FAILED_TO_RETRIEVE_TICKETS;
import static com.zendesk.ticket.viewer.error.Error.PROPERTIES_FILE_ERROR;

import com.zendesk.ticket.viewer.TicketViewer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;
import org.json.JSONObject;

public class NetworkService {

  private String subDomain = "", email = "", password = "";

  public NetworkService() {
    //Get the properties and be ready for connection
    try (InputStream input = TicketViewer.class.getClassLoader()
        .getResourceAsStream("connectionInfo.properties")) {
      Properties prop = new Properties();

      if (input == null) {
        System.out.println(PROPERTIES_FILE_ERROR.getErrorMessage());
        return;
      }

      //load a properties file from class path, inside static method
      prop.load(input);

      //get the property value and print it out
      subDomain = prop.getProperty("connection.subdomain");
      email = prop.getProperty("connection.email");
      password = prop.getProperty("connection.password");

      if (subDomain.isEmpty() || email.isEmpty() || password.isEmpty()) {
        System.out.println(PROPERTIES_FILE_ERROR.getErrorMessage());
        return;
      }

    } catch (IOException ex) {
      System.out.println(PROPERTIES_FILE_ERROR.getErrorMessage());
      ex.printStackTrace();
    }
  }

  public JSONObject zenDeskAPICall(String resourcePath) {
    String ticketUrl = "https://" + subDomain + ".zendesk.com/api/v2/" + resourcePath;

    URL url;
    try {
      //initialize url and connection object
      url = new URL(ticketUrl);
      URLConnection urlConnection = url.openConnection();

      //encode authentication details
      String basicAuth = authenticateUser(email, password);

      //set authentication details
      urlConnection.setRequestProperty("Authorization", basicAuth);

      //connect to the URL with authorization
      urlConnection.connect();

      //get the data as an InputStream
      InputStream inputStream = urlConnection.getInputStream();

      Scanner sc = new Scanner(inputStream);
      sc.useDelimiter("\\A");
      String result = sc.hasNext() ? sc.next() : "";
      sc.close();

      return new JSONObject(result);

    } catch (Exception ex) {
      System.out.println(FAILED_TO_RETRIEVE_TICKETS.getErrorMessage());
      return null;
    }
  }

  public String authenticateUser(String email, String password) {
    String userAuthentication = email + ":" + password;
    return "Basic " + javax.xml.bind.DatatypeConverter
        .printBase64Binary(userAuthentication.getBytes());
  }


}
