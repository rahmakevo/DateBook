package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import spencerstudios.com.bungeelib.Bungee;

public class MatchProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        ImageView mImageBack = findViewById(R.id.imageBackMatchProfile);
        mImageBack.setOnClickListener(view -> {
            Intent mBackIntent = new Intent(this, HomeActivity.class);
            startActivity(mBackIntent);
            Bungee.slideRight(this);
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
