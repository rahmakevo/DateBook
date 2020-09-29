package com.example.datebook.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.datebook.R;

import spencerstudios.com.bungeelib.Bungee;

public class NotificationsSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);

        ImageView imageBack = findViewById(R.id.imageBackSettingsNotifyPref);
        imageBack.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, SettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideRight(this);
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsNotify, new PreferencesNotifyFragment())
                .commit();
    }

    public static class PreferencesNotifyFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_notifications, rootKey);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(this, SettingsActivity.class);
        startActivity(mIntent);
        Bungee.slideRight(this);
    }
}
