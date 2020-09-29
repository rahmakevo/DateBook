package com.example.datebook.model;

public class MatchModel {
    public String user_id;
    public String public_name;
    public String thumb_profile;

    public MatchModel() { }

    public MatchModel(String user_id, String public_name, String thumb_profile) {
        this.user_id = user_id;
        this.public_name = public_name;
        this.thumb_profile = thumb_profile;
    }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public String getPublic_name() { return public_name; }
    public void setPublic_name(String public_name) { this.public_name = public_name; }

    public String getThumb_profile() { return thumb_profile; }
    public void setThumb_profile(String thumb_profile) { this.thumb_profile = thumb_profile; }

}
