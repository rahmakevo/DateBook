package com.example.datebook.model;

public class SettingsModel {
    public String name;
    public int icon;

    public SettingsModel() { }

    public SettingsModel(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getIcon() { return icon; }
    public void setIcon(int icon) { this.icon = icon; }
}
