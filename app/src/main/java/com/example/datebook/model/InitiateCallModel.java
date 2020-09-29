package com.example.datebook.model;

public class InitiateCallModel {
    public String caller_id;
    public String date;
    public String recipient_id;

    public InitiateCallModel() { }

    public InitiateCallModel(String caller_id, String date, String recipient_id) {
        this.caller_id = caller_id;
        this.date = date;
        this.recipient_id = recipient_id;
    }

    public String getCaller_id() { return caller_id; }
    public void setCaller_id(String caller_id) { this.caller_id = caller_id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getRecipient_id() { return recipient_id; }
    public void setRecipient_id(String recipient_id) { this.recipient_id = recipient_id; }
}
