package com.example.datebook.model;

public class InitiateChatModel {

    public String sender_id;
    public String recipient_id;
    public String date;

    public InitiateChatModel() { }

    public InitiateChatModel(String sender_id, String recipient_id, String date) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.date = date;
    }

    public String getRecipient_id() { return recipient_id; }
    public void setRecipient_id(String recipient_id) { this.recipient_id = recipient_id; }

    public String getSender_id() { return sender_id; }
    public void setSender_id(String sender_id) { this.sender_id = sender_id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

}
