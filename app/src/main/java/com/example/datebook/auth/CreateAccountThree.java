package com.example.datebook.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datebook.HomeActivity;
import com.example.datebook.R;
import com.example.datebook.model.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import spencerstudios.com.bungeelib.Bungee;


public class CreateAccountThree extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mBirthDb;
    private DatabaseReference mBirthRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_account_three);


        mAuth = FirebaseAuth.getInstance();
        mBirthDb = FirebaseDatabase.getInstance();
        mBirthRef = mBirthDb.getReference();

        DatePicker mDatePicker = findViewById(R.id.datePicker);
        Button btnDatePicker = findViewById(R.id.buttonProceedDob);
        btnDatePicker.setOnClickListener(view -> {
            int mDay = mDatePicker.getDayOfMonth();
            int mMonth = mDatePicker.getMonth();
            int mYear = mDatePicker.getYear();

            if (isAgeValid(mYear)) {
                String mDob = mDay + "/" + mMonth + "/" + mYear;
                FirebaseUser mUser = mAuth.getCurrentUser();

                mBirthRef.child("users").child("profile").child(mUser.getUid()).child("dob")
                        .setValue(mDob).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent mIntent = new Intent(this, HomeActivity.class);
                                startActivity(mIntent);
                                Bungee.slideLeft(this);
                            } else {
                                Toast.makeText(this, "Dear Customer, There has been a network issue. Kindly try again!", Toast.LENGTH_SHORT).show();
                            }
                         });

            } else {
                Toast.makeText(this, "Dear Customer, You are too young ", Toast.LENGTH_SHORT).show();
            }

        });

        ImageView mImageBack = findViewById(R.id.createAccountThreeBackButton);
        mImageBack.setOnClickListener(view -> {
            finishAffinity();
        });
    }

    private boolean isAgeValid(int mYear) {
        // check for age above 13 years
        // above age
        return mYear <= 2007;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
