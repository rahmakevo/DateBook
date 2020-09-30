package com.example.datebook.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.datebook.R;
import com.example.datebook.adapter.ViewPagerFragmentAdapter;
import com.example.datebook.settings.SettingsActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends AppCompatActivity {
    protected ViewPagerFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        managePresence();

        TabLayout mHomeLayout = findViewById(R.id.tabLayoutHome);
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Home"));
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Calls"));
        ViewPager viewPager2 = findViewById(R.id.viewPagerHome);

        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(adapter);
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mHomeLayout));
        mHomeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Spinner spinner = findViewById(R.id.spinner_language_home);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.language_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter2);

        ImageView mImageMore = findViewById(R.id.imageViewMore);
        mImageMore.setOnClickListener(view -> {
            Intent mIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideLeft(this);
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mUserPrivacySettingsDb = FirebaseDatabase.getInstance();
        DatabaseReference mUserPrivacySettingsRef = mUserPrivacySettingsDb.getReference();
        mUserPrivacySettingsRef
                .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                .child("settings").child("privacySettings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    HashMap<String, String> mUserPrivacySettingsMap = new HashMap<>();
                    mUserPrivacySettingsMap.put("lastSeen", "Everyone");
                    mUserPrivacySettingsMap.put("profileImage", "Everyone");
                    mUserPrivacySettingsMap.put("status", "Everyone");

                    mUserPrivacySettingsRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("settings").child("privacySettings").setValue(mUserPrivacySettingsMap)
                            .addOnSuccessListener(task -> {});
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void managePresence() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mPresenceDb = FirebaseDatabase.getInstance();
        DatabaseReference mPresenceRef = mPresenceDb.getReference();

        // set default value for user presence
        mPresenceRef
                .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                .child("userPresence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    mPresenceRef
                            .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                            .child("userPresence").setValue(true).addOnSuccessListener(taskPresence -> {
                                // create and save deviceToken
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                Date mDate = new Date();
                                String mDateFormat = SimpleDateFormat.getDateTimeInstance().format(mDate);

                                HashMap<String, String> mDeviceTokenMap = new HashMap<>();
                                mDeviceTokenMap.put("device_token", token_id);
                                mDeviceTokenMap.put("date", mDateFormat);

                                mPresenceRef
                                        .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                        .child("deviceToken").setValue(mDeviceTokenMap).addOnSuccessListener(taskToken -> {});
                    });
                } else {
                    if (snapshot.getValue().equals(false)) {
                        mPresenceRef
                                .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                .child("userPresence").setValue(true).addOnSuccessListener(taskPresence -> { });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mPresenceRef
                .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                .child("userPresence").onDisconnect().removeValue().addOnSuccessListener(taskPresence -> {

            mPresenceRef
                    .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                    .child("deviceToken").removeValue().addOnSuccessListener(taskRemoveToken -> {});
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        managePresence();
    }

    @Override
    protected void onResume() {
        super.onResume();
        managePresence();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        managePresence();
    }
}
