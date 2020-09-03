package com.example.datebook.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.datebook.R;

import spencerstudios.com.bungeelib.Bungee;

public class PreferencesMatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_preferences_layout);

        ImageView mImageBack = findViewById(R.id.imageBackSettingsMatchPref);
        mImageBack.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, SettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideRight(this);
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new PreferencesMatchesFragment())
                .commit();
    }

    public static class PreferencesMatchesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_matches, rootKey);
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
