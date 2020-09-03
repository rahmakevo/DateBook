package com.example.datebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class MatchProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mStorageAccRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        progressBar = findViewById(R.id.progressProfileMatch);
        progressBar.setVisibility(View.VISIBLE);
        String user_id = getIntent().getStringExtra("user_id");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mStorageAccRef = mFirebaseDb.getReference();

        ImageView mImageBack = findViewById(R.id.imageBackMatchProfile);
        mImageBack.setOnClickListener(view -> {
            Intent mBackIntent = new Intent(this, HomeActivity.class);
            startActivity(mBackIntent);
            Bungee.slideRight(this);
        });

        ImageView mImageMain = findViewById(R.id.imageViewMain);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(user_id).child("main.jpg");
        mStorageRef.getDownloadUrl().addOnSuccessListener(snapShot -> {
            progressBar.setVisibility(View.GONE);
            Picasso.get().load(snapShot).into(mImageMain);
        });

        TextView mTextNamePublic = findViewById(R.id.textMatchProfileName);
        TextView mTextStatus = findViewById(R.id.textMatchProfileStatus);

        mStorageAccRef.child("users").child("profile").child(user_id).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mTextNamePublic.setText(snapshot.child("publicName").getValue().toString());
                        mTextStatus.setText(snapshot.child("status").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        CircleImageView mInitiateChat = findViewById(R.id.imageViewInitiateChatWithMatch);
        mInitiateChat.setOnClickListener(view -> {
            mStorageAccRef.child("chat").child(mAuth.getCurrentUser().getUid()).child("initiateChat")
                    .child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Date date = new Date();
                        HashMap<String, String> mInitiateChatMap = new HashMap<>();
                        mInitiateChatMap.put("recipient_id", user_id);
                        mInitiateChatMap.put("sender_id", mAuth.getCurrentUser().getUid());
                        mInitiateChatMap.put("date", String.valueOf(date));

                        mStorageAccRef.child("chat").child(mAuth.getCurrentUser().getUid()).child("initiateChat")
                                .child(user_id).setValue(mInitiateChatMap).addOnSuccessListener(snapShot -> {});
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        CircleImageView mInitiateVideoCall = findViewById(R.id.imageViewInitiateVideoCallWithMatch);
        mInitiateVideoCall.setOnClickListener(view -> {
            mStorageAccRef.child("users").child("calls").child(mAuth.getCurrentUser().getUid())
                    .child("initiateCall").child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Date date = new Date();
                        HashMap<String, String> mInitiateVideoChatMap = new HashMap<>();
                        mInitiateVideoChatMap.put("caller_id", mAuth.getCurrentUser().getUid());
                        mInitiateVideoChatMap.put("date", String.valueOf(date));
                        mInitiateVideoChatMap.put("recipient_id", user_id);

                        mStorageAccRef.child("users").child("calls").child(mAuth.getCurrentUser().getUid())
                                .child("initiateCall").child(user_id).setValue(mInitiateVideoChatMap).addOnSuccessListener(snapShot -> {});

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mBackIntent = new Intent(this, HomeActivity.class);
        startActivity(mBackIntent);
        Bungee.slideRight(this);
    }
}
