package com.zendesk.ticket.viewer.models;

import java.util.Date;

public class Ticket {

    protected int id;
    protected String status;
    protected String title;
    protected String description;
    protected Date updated_on;
    protected int assigned_to;
    protected int create_by;

    public Ticket() {
        this.id = 0;
        this.status = "Not available";
        this.title = "Not available";
        this.description = "Not available";
        this.assigned_to = 0;
        this.create_by = 0;
        this.updated_on = null;
    }

    public Ticket(int id, String status, String title, String description, Date updated_on, int assigned_to, int create_by) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.updated_on = updated_on;
        this.assigned_to = assigned_to;
        this.create_by = create_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(int assigned_to) {
        this.assigned_to = assigned_to;
    }

    public int getCreate_by() {
        return create_by;
    }

    public void setCreate_by(int create_by) {
        this.create_by = create_by;
    }


    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }

    @Override
    public String toString() {
        String ret = "\n-------------\n Ticket: ";
        if (id != 0) {
            ret += id;
        } else {
            ret += "Not available";
        }
        ret += " [" + status + "] " +
                " " + title +
                "\n Description: " + description;
        if (assigned_to == 0) {
            ret += "\n\t Assigned to: Not Assigned";
        } else {
            ret += "\n\t Assigned to: " + assigned_to;
        }
        if (create_by != 0) {
            ret += "\n\t Created by: " + create_by;
        } else {
            ret += "\n\t Created by: Not Available";
        }
        if (updated_on != null)
            ret += "\n\t updated on: " + updated_on;

        ret += "\n==============\n";
        return ret;
    }
}
