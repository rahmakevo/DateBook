package com.example.datebook.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class CreateAccountTwo extends AppCompatActivity {

    private CircleImageView maleAvatar;
    private CircleImageView malePickAvatar;

    private CircleImageView femaleAvatar;
    private CircleImageView femalePickAvatar;
    private String mPublicName;

    private Boolean isMaleSelected = false;
    private  Boolean isFemaleSelected = false;

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mProfileDb;
    protected DatabaseReference mProfileRef;

    public CreateAccountTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_create_account_two);

        mAuth = FirebaseAuth.getInstance();
        mProfileDb = FirebaseDatabase.getInstance();
        mProfileRef = mProfileDb.getReference();

        maleAvatar = findViewById(R.id.avatar_image_male);
        malePickAvatar = findViewById(R.id.icon_pick_male);

        femaleAvatar = findViewById(R.id.avatar_image_female);
        femalePickAvatar = findViewById(R.id.icon_pick_female);

        maleAvatar.setOnClickListener(v -> {
            isMaleSelected = true;
            isFemaleSelected = false;
            malePickAvatar.setVisibility(View.VISIBLE);
            femalePickAvatar.setVisibility(View.GONE);
        });

        femaleAvatar.setOnClickListener(v -> {
            isFemaleSelected = true;
            isMaleSelected = false;
            femalePickAvatar.setVisibility(View.VISIBLE);
            malePickAvatar.setVisibility(View.GONE);
        });

        FirebaseUser mUser = mAuth.getCurrentUser();
        mProfileRef.child("users").child("profile").child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            mPublicName = snapshot.child("publicName").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Button btnProceed = findViewById(R.id.buttonProceedGender);
        btnProceed.setOnClickListener(view -> {

            if (!isMaleSelected && !isFemaleSelected) {
                Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
            } else {

                if (isMaleSelected) {

                    mProfileRef.child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("male").addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Date objDate = new Date();

                                    HashMap<String, String> mGenderMap = new HashMap<>();
                                    mGenderMap.put("date", objDate.toString());
                                    mGenderMap.put("public_name", mPublicName);
                                    mGenderMap.put("thumb_profile", String.valueOf(mUser.getPhotoUrl()));
                                    mGenderMap.put("user_id", mUser.getUid());
                                    mProfileRef.child("users").child("matches").child("male").child(mUser.getUid())
                                            .setValue(mGenderMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Intent mIntent = new Intent(this, CreateAccountThree.class);
                                            startActivity(mIntent);
                                            Bungee.slideLeft(this);
                                        } else {
                                            Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                }
                            });

                } else if (isFemaleSelected) {
                    mProfileRef.child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("female").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Date objDate = new Date();

                            HashMap<String, String> mGenderMap = new HashMap<>();
                            mGenderMap.put("date", objDate.toString());
                            mGenderMap.put("public_name", mPublicName);
                            mGenderMap.put("thumb_profile", String.valueOf(mUser.getPhotoUrl()));
                            mGenderMap.put("user_id", mUser.getUid());
                            mProfileRef.child("users").child("matches").child("female").child(mUser.getUid())
                                    .setValue(mGenderMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Intent mIntent = new Intent(this, CreateAccountThree.class);
                                    startActivity(mIntent);
                                    Bungee.slideLeft(this);
                                } else {
                                    Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });

        ImageView mImageBack = findViewById(R.id.createAccountTwoBackButton);
        mImageBack.setOnClickListener(view -> {
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
