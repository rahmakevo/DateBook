package com.example.datebook.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.datebook.R;

import spencerstudios.com.bungeelib.Bungee;

public class ProfileSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        ImageView mImageBack = findViewById(R.id.imageBackSettingsProfilePref);
        mImageBack.setOnClickListener(view -> {
            Intent mIntentBack = new Intent(this, SettingsActivity.class);
            startActivity(mIntentBack);
            Bungee.slideRight(this);
        });
    }
}
