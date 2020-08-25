package com.example.datebook.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;

import com.example.datebook.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import spencerstudios.com.bungeelib.Bungee;

public class CreateAccountOne extends AppCompatActivity {
    private TextInputEditText mProfileName;
    private TextInputLayout mProfileNameTextLayout;

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mProfileDb;
    protected DatabaseReference mProfileRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_create_account_one);

        mAuth = FirebaseAuth.getInstance();
        mProfileDb = FirebaseDatabase.getInstance();
        mProfileRef = mProfileDb.getReference();

        ImageView mBackPressed = findViewById(R.id.createAccountOneBackButton);
        mBackPressed.setOnClickListener(v -> {
            finishAffinity();
        });

        mProfileName = findViewById(R.id.editTextProfileName);
        mProfileNameTextLayout = findViewById(R.id.editTextProfileNameLayout);

        mProfileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mProfileNameTextLayout.setError("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProfileNameTextLayout.setError("");
            }
        });

        Button btnProfileName = findViewById(R.id.buttonProceedName);
        btnProfileName.setOnClickListener(v -> {
            String profileName = mProfileName.getText().toString();

            if (profileName.isEmpty()) {
                mProfileNameTextLayout.setError("Dear Customer, Profile Name cannot be empty");
            } else {
                // validate according to policy standard
                // save name to db in case user does not finish setup
                FirebaseUser mUser = mAuth.getCurrentUser();
                mProfileRef.child("users").child("profile").child(mUser.getUid()).child("publicName")
                        .setValue(profileName).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Intent mIntent = new Intent(this, CreateAccountTwo.class);
                                startActivity(mIntent);
                                Bungee.slideLeft(this);
                            } else {
                                mProfileNameTextLayout.setError(task.getException().getMessage());
                            }
                        }
                );
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
