package com.example.datebook.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class CreateAccountTwo extends AppCompatActivity {

    private CircleImageView maleAvatar;
    private CircleImageView malePickAvatar;

    private CircleImageView femaleAvatar;
    private CircleImageView femalePickAvatar;

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

        Button btnProceed = findViewById(R.id.buttonProceedGender);
        btnProceed.setOnClickListener(view -> {

            if (!isMaleSelected && !isFemaleSelected) {
                Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
            } else {

                if (isMaleSelected) {
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    mProfileRef.child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("male").addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Intent mIntent = new Intent(this, CreateAccountThree.class);
                                    startActivity(mIntent);
                                    Bungee.slideLeft(this);
                                } else {
                                    Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                }
                            });

                } else if (isFemaleSelected) {
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    mProfileRef.child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("female").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent mIntent = new Intent(this, CreateAccountThree.class);
                            startActivity(mIntent);
                            Bungee.slideLeft(this);
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
