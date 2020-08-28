package com.example.datebook.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.datebook.R;

import spencerstudios.com.bungeelib.Bungee;

public class PrivacySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        ImageView imageBack = findViewById(R.id.imageBackSettingsPrivacyPref);
        imageBack.setOnClickListener(view -> {
            Intent mIntentBack = new Intent(this, AccountSettingsActivity.class);
            startActivity(mIntentBack);
            Bungee.slideRight(this);
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsPrivacy, new PreferencesPrivacyFragment())
                .commit();
    }

    public static class PreferencesPrivacyFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_privacy, rootKey);
        }
    }
}
