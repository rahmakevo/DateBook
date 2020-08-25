package com.example.datebook.model;

public class ChatModel {

    public String user_id;
    public String recipient_id;
    public String public_name;

    public ChatModel() { }

    public ChatModel(String user_id, String recipient_id, String public_name) {
        this.user_id = user_id;
        this.recipient_id = recipient_id;
        this.public_name = public_name;
    }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getRecipient_id() { return recipient_id; }
    public void setRecipient_id(String recipient_id) { this.recipient_id = recipient_id; }

    public String getPublic_name() { return public_name; }
    public void setPublic_name(String public_name) { this.public_name = public_name; }

}
