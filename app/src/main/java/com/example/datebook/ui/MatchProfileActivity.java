package com.example.datebook.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datebook.R;
import com.example.datebook.adapter.SliderAdapterExample;
import com.example.datebook.model.ProfileImageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class MatchProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mStorageAccRef;
    private ProgressBar progressBar;

    private List<ProfileImageModel> modelList = new ArrayList<>();
    private SliderAdapterExample adapterExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        progressBar = findViewById(R.id.progressProfileMatch);
        progressBar.setVisibility(View.VISIBLE);
        String user_id = getIntent().getStringExtra("user_id");
        adapterExample = new SliderAdapterExample(MatchProfileActivity.this, modelList);
        initAdapter();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mStorageAccRef = mFirebaseDb.getReference();

        ImageView mImageBack = findViewById(R.id.imageBackMatchProfile);
        mImageBack.setOnClickListener(view -> {
            Intent mBackIntent = new Intent(this, HomeActivity.class);
            startActivity(mBackIntent);
            Bungee.slideRight(this);
        });

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(user_id).child("main.jpg");
        mStorageRef.getDownloadUrl().addOnCompleteListener(snapShot -> {
            if (snapShot.isSuccessful()) {
                ProfileImageModel model = new ProfileImageModel(String.valueOf(snapShot));
                modelList.add(model);
                adapterExample.notifyDataSetChanged();
            }
        });

        StorageReference mStorageTwoRef = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(user_id).child("two.jpg");
        mStorageTwoRef.getDownloadUrl().addOnCompleteListener(snapShot -> {
            if (snapShot.isSuccessful()) {
                ProfileImageModel model = new ProfileImageModel(String.valueOf(snapShot));
                modelList.add(model);
                adapterExample.notifyDataSetChanged();
            }
        });

        StorageReference mStorageThreeRef = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(user_id).child("three.jpg");
        mStorageThreeRef.getDownloadUrl().addOnCompleteListener(snapShot -> {
            if (snapShot.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                ProfileImageModel model = new ProfileImageModel(String.valueOf(snapShot));
                modelList.add(model);
                adapterExample.notifyDataSetChanged();
            }
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
            mStorageAccRef
                    .child("users").child("chat").child(mAuth.getCurrentUser().getUid()).child("initiateChat")
                    .child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Date date = new Date();
                        HashMap<String, String> mInitiateChatMap = new HashMap<>();
                        mInitiateChatMap.put("recipient_id", user_id);
                        mInitiateChatMap.put("sender_id", mAuth.getCurrentUser().getUid());
                        mInitiateChatMap.put("date", String.valueOf(date));

                        mStorageAccRef.child("users").child("chat").child(mAuth.getCurrentUser().getUid()).child("initiateChat")
                                .child(user_id).setValue(mInitiateChatMap).addOnSuccessListener(snapShot -> {

                            HashMap<String, String> mInitiateRecipientChatMap = new HashMap<>();
                            mInitiateRecipientChatMap.put("recipient_id", mAuth.getCurrentUser().getUid());
                            mInitiateRecipientChatMap.put("sender_id", user_id);
                            mInitiateRecipientChatMap.put("date", String.valueOf(date));

                            mStorageAccRef.child("users").child("chat").child(user_id).child("initiateChat")
                                    .child(mAuth.getCurrentUser().getUid()).setValue(mInitiateRecipientChatMap).addOnSuccessListener(taskRecipient -> {
                                Intent mIntent = new Intent(MatchProfileActivity.this, MessageActivity.class);
                                mIntent.putExtra("recipient_id", user_id);
                                startActivity(mIntent);
                                Bungee.slideLeft(MatchProfileActivity.this);
                            });
                        });
                    } else {
                        Intent mIntent = new Intent(MatchProfileActivity.this, MessageActivity.class);
                        mIntent.putExtra("recipient_id", user_id);
                        startActivity(mIntent);
                        Bungee.slideLeft(MatchProfileActivity.this);
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
                                .child("initiateCall").child(user_id).setValue(mInitiateVideoChatMap).addOnSuccessListener(snapShot -> {

                                    HashMap<String, String> mInitiateRecipientVideoChatMap = new HashMap<>();
                                    mInitiateRecipientVideoChatMap.put("caller_id", mAuth.getCurrentUser().getUid());
                                    mInitiateRecipientVideoChatMap.put("date", String.valueOf(date));
                                    mInitiateRecipientVideoChatMap.put("recipient_id", user_id);

                                    mStorageAccRef.child("users").child("calls").child(user_id).child("initiateCall")
                                            .child(mAuth.getCurrentUser().getUid()).setValue(mInitiateRecipientVideoChatMap).addOnSuccessListener(taskRecipient -> {});

                        });

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

    private void initAdapter() {
        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(adapterExample);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(30); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }
}
