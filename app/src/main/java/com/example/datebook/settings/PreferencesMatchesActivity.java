package com.example.datebook.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.datebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import spencerstudios.com.bungeelib.Bungee;

public class PreferencesMatchesActivity extends AppCompatActivity {
    private static FirebaseDatabase mMatchPrefDb;
    private static DatabaseReference mMatchPrefRef;
    private static FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();
        mMatchPrefDb = FirebaseDatabase.getInstance();
        mMatchPrefRef = mMatchPrefDb.getReference();
    }

    public static class PreferencesMatchesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_matches, rootKey);

            SwitchPreferenceCompat mDiscoverable = getPreferenceManager().findPreference("discovery");
            Objects.requireNonNull(mDiscoverable).setOnPreferenceChangeListener((preference, newValue) -> {
                mMatchPrefRef
                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                        .child("settings").child("matchPreferences").child("discovery")
                        .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                return false;
            });
            CheckBoxPreference mMaleCheckbox = getPreferenceManager().findPreference("male");
            Objects.requireNonNull(mMaleCheckbox).setOnPreferenceChangeListener(((preference, newValue) -> {
                if (newValue.toString().equals("true")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("matchGenderType")
                            .setValue("male").addOnSuccessListener(task -> {});
                } else if (newValue.toString().equals("false")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("matchGenderType")
                            .setValue("female").addOnSuccessListener(task -> {});
                }
                return false;
            }));
            CheckBoxPreference mFemaleCheckbox = getPreferenceManager().findPreference("female");
            Objects.requireNonNull(mFemaleCheckbox).setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue.toString().equals("true")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("matchGenderType")
                            .setValue("female").addOnSuccessListener(task -> {});
                } else if (newValue.toString().equals("false")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("matchGenderType")
                            .setValue("male").addOnSuccessListener(task -> {});
                }
                return false;
            });
            SeekBarPreference mAgePrefSeekBar = getPreferenceManager().findPreference("show_ages");
            Objects.requireNonNull(mAgePrefSeekBar).setOnPreferenceChangeListener((preference, newValue) -> {
                mMatchPrefRef
                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                        .child("settings").child("matchPreferences").child("agePref")
                        .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                return false;
            });
            CheckBoxPreference mCountryFilter = getPreferenceManager().findPreference("country");
            Objects.requireNonNull(mCountryFilter).setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue.toString().equals("true")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("countryFilter")
                            .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                } else if (newValue.toString().equals("false")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("countryFilter")
                            .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                }
                return false;
            });
            CheckBoxPreference mLocaleFilter = getPreferenceManager().findPreference("locality");
            mLocaleFilter.setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue.toString().equals("true")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("localityFilter")
                            .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                } else if (newValue.toString().equals("false")) {
                    mMatchPrefRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("matchPreferences").child("localityFilter")
                            .setValue(newValue.toString()).addOnSuccessListener(task -> {});
                }
                return false;
            });

            mMatchPrefRef
                    .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                    .child("settings").child("matchPreferences").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String discoverable = snapshot.child("discovery").getValue().toString();
                    if (discoverable.equals("true")) {
                        mDiscoverable.setChecked(true);
                    } else {
                        mDiscoverable.setChecked(false);
                    }

                    String genderType = snapshot.child("matchGenderType").getValue().toString();
                    if (genderType.equals("male")) {
                        mMaleCheckbox.setChecked(true);
                    } else if (genderType.equals("female")) {
                        mFemaleCheckbox.setChecked(true);
                    }

                    String agePref = snapshot.child("agePref").getValue().toString();
                    mAgePrefSeekBar.setValue(Integer.parseInt(agePref));

                    String countryFilter = snapshot.child("countryFilter").getValue().toString();
                    if (countryFilter.equals("true")) {
                        mCountryFilter.setChecked(true);
                    } else if (countryFilter.equals("false")) {
                        mCountryFilter.setChecked(false);
                    }

                    String localeFilter = snapshot.child("localityFilter").getValue().toString();
                    if (localeFilter.equals("true")) {
                        mLocaleFilter.setChecked(true);
                    } else if (localeFilter.equals("false")) {
                        mLocaleFilter.setChecked(false);
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
        Intent mIntent = new Intent(this, SettingsActivity.class);
        startActivity(mIntent);
        Bungee.slideRight(this);
    }
}
