package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import spencerstudios.com.bungeelib.Bungee;

public class SettingsActivity extends AppCompatActivity {

    private ListView mListSettings;
    private String mSettingsName[] = {
            "Help",
            "Profile",
            "Account",
            "Chats",
            "Notifications"
    };
    private int mSettingsIcon[] = {
            R.drawable.ic_help_black_24dp,
            R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_vpn_key_black_24dp,
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_notifications_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView imageBack = findViewById(R.id.imageBackSettings);
        imageBack.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, HomeActivity.class);
            startActivity(mIntent);
            Bungee.slideRight(this);
        });

        mListSettings = findViewById(R.id.list_settings);
    }
}
