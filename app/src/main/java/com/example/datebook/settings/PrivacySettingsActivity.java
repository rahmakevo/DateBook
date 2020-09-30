package com.example.datebook.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.datebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class PrivacySettingsActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private static FirebaseDatabase mPrivacySettingsDb;
    private static DatabaseReference mPrivacySettingsRef;
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

            mAuth = FirebaseAuth.getInstance();
            mPrivacySettingsDb = FirebaseDatabase.getInstance();
            mPrivacySettingsRef = mPrivacySettingsDb.getReference();

            ListPreference mLastSeenPref = getPreferenceManager().findPreference("last_seen");
            mLastSeenPref.setOnPreferenceChangeListener((preference, newValue) -> {
                mPrivacySettingsRef
                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                        .child("settings").child("privacySettings").child("lastSeen").setValue(newValue)
                        .addOnSuccessListener(task -> {});
                return false;
            });

            ListPreference mProfilePref = getPreferenceManager().findPreference("profile_gallery");
            mProfilePref.setOnPreferenceChangeListener((preference, newValue) -> {
                mPrivacySettingsRef
                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                        .child("settings").child("privacySettings").child("profileImage").setValue(newValue)
                        .addOnSuccessListener(task -> {});
                return false;
            });

            ListPreference mStatusPref = getPreferenceManager().findPreference("status");
            mStatusPref.setOnPreferenceChangeListener((preference, newValue) -> {
                mPrivacySettingsRef
                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                        .child("settings").child("privacySettings").child("status").setValue(newValue)
                        .addOnSuccessListener(task -> {});
                return false;
            });

            mPrivacySettingsRef
                    .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                    .child("settings").child("privacySettings").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        mLastSeenPref.setValue(snapshot.child("lastSeen").getValue().toString());
                        mProfilePref.setValue(snapshot.child("profileImage").getValue().toString());
                        mStatusPref.setValue(snapshot.child("status").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntentBack = new Intent(this, AccountSettingsActivity.class);
        startActivity(mIntentBack);
        Bungee.slideRight(this);
    }
}
