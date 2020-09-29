package com.example.datebook.model;

public class MessageModel {
    public String from;
    public String message;
    public String time;
    public String to;

    public MessageModel() { }

    public MessageModel(String from, String message, String time, String to) {
        this.from = from;
        this.message = message;
        this.time = time;
        this.to = to;
    }

    public String getFrom() { return from; }

    public void setFrom(String from) { this.from = from; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getTo() { return to; }

    public void setTo(String to) { this.to = to; }
}
