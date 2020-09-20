package com.example.datebook.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datebook.ui.HomeActivity;
import com.example.datebook.R;
import com.example.datebook.adapter.SettingsListRecyclerView;
import com.example.datebook.model.SettingsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class SettingsActivity extends AppCompatActivity {

    private List<SettingsModel> modelList = new ArrayList<>();
    private SettingsListRecyclerView adapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mUserDb;
    private DatabaseReference mUserRef;
    private ProgressBar mProgressSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        mUserDb = FirebaseDatabase.getInstance();
        mUserRef = mUserDb.getReference();

        mProgressSettings = findViewById(R.id.progressSettings);
        mProgressSettings.setVisibility(View.VISIBLE);

        CircleImageView mSettingsProfile = findViewById(R.id.imageViewSettingsProfile);
        TextView mSettingsProfileName = findViewById(R.id.textSettingsName);
        TextView mSettingsProfileStatus = findViewById(R.id.textSettingsStatus);

        mUserRef.child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mSettingsProfileName.setText(snapshot.child("publicName").getValue().toString());
                        mSettingsProfileStatus.setText(snapshot.child("status").getValue().toString());
                        Picasso.get().load(snapshot.child("publicThumbnail").getValue().toString())
                                .into(mSettingsProfile);
                        mProgressSettings.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        ImageView imageBack = findViewById(R.id.imageBackSettings);
        imageBack.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, HomeActivity.class);
            startActivity(mIntent);
            Bungee.slideRight(this);
        });

        RecyclerView mListSettings = findViewById(R.id.list_settings);
        adapter = new SettingsListRecyclerView(modelList, getSupportFragmentManager());
        mListSettings.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mListSettings.addItemDecoration(itemDecor);

        mListSettings.setAdapter(adapter);
        prepareSettingsData();

        ConstraintLayout mLayoutProfile = findViewById(R.id.layout_profile);
        mLayoutProfile.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, ProfileSettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideLeft(this);
        });

    }

    private void prepareSettingsData() {
        SettingsModel model = new SettingsModel("Help", R.drawable.ic_help_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Profile", R.drawable.ic_account_circle_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Account", R.drawable.ic_vpn_key_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Chats", R.drawable.ic_chat_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Notifications", R.drawable.ic_notifications_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Match Preferences", R.drawable.ic_favorite_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Invite a friend", R.drawable.ic_people_black_24dp);
        modelList.add(model);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(this, HomeActivity.class);
        startActivity(mIntent);
        Bungee.slideRight(this);
    }
}
