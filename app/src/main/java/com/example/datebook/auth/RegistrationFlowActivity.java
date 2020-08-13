package com.example.datebook.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.datebook.HomeActivity;
import com.example.datebook.R;
import com.example.datebook.fragments.CreateAccountOne;
import com.example.datebook.fragments.CreateAccountThree;
import com.example.datebook.fragments.CreateAccountTwo;
import com.example.datebook.model.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import spencerstudios.com.bungeelib.Bungee;

public class RegistrationFlowActivity extends AppCompatActivity {
    protected MainViewModel mainViewModel;
    FrameLayout mFragmentLayout;

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mProfileDb;
    protected DatabaseReference mProfileRef;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_flow);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mFragmentLayout = findViewById(R.id.auth_fragment_container);
        mAuth = FirebaseAuth.getInstance();
        mProfileDb = FirebaseDatabase.getInstance();
        mProfileRef = mProfileDb.getReference();

        String genderNull = mIntent.getStringExtra("isGenderNull");
        String dobNull = mIntent.getStringExtra("isDobNull");

        if (genderNull.equals("true")) {
            CreateAccountTwo mFragment = new CreateAccountTwo();
            FragmentManager mManager = getSupportFragmentManager();
            mManager.beginTransaction()
                    .replace(R.id.auth_fragment_container, mFragment)
                    .addToBackStack(CreateAccountTwo.class.getSimpleName())
                    .commit();
        }

        if (dobNull.equals("true")) {
            CreateAccountThree mFragment = new CreateAccountThree();
            FragmentManager mManager = getSupportFragmentManager();
            mManager.beginTransaction()
                    .replace(R.id.auth_fragment_container, mFragment)
                    .addToBackStack(CreateAccountThree.class.getSimpleName())
                    .commit();
        }

        mainViewModel.getIsFirstTimeUser().observe(this, mFirstTimeUser -> {
            if (mFirstTimeUser) {
                CreateAccountOne mFragment = new CreateAccountOne();
                FragmentManager mManger = getSupportFragmentManager();
                mManger.beginTransaction()
                        .add(R.id.auth_fragment_container, mFragment)
                        .addToBackStack(CreateAccountOne.class.getSimpleName())
                        .commit();
            }
        });

        mainViewModel.getPublicName().observe(this, mPublicName -> {
            if (!mPublicName.isEmpty()) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                mProfileRef.child("users").child("profile").child("account")
                        .child(mUser.getUid()).child("publicName")
                        .setValue(mPublicName).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                CreateAccountTwo mFragment = new CreateAccountTwo();
                                FragmentManager mManager = getSupportFragmentManager();
                                mManager.beginTransaction()
                                        .replace(R.id.auth_fragment_container, mFragment)
                                        .addToBackStack(CreateAccountTwo.class.getSimpleName())
                                        .commit();
                            }
                        }
                );
            }
        });

        mainViewModel.getSelectedGender().observe(this, gender -> {
           if (!gender.isEmpty()) {
               FirebaseUser mUser = mAuth.getCurrentUser();
               mProfileRef.child("users").child("profile").child("account")
                       .child(mUser.getUid()).child("gender")
                       .setValue(gender).addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               CreateAccountThree mFragment = new CreateAccountThree();
                               FragmentManager mManager = getSupportFragmentManager();
                               mManager.beginTransaction()
                                       .replace(R.id.auth_fragment_container, mFragment)
                                       .addToBackStack(CreateAccountThree.class.getSimpleName())
                                       .commit();
                           }
               });
           }
        });

        mainViewModel.getDobSelected().observe(this, dob -> {
            if (!dob.isEmpty()) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                mProfileRef.child("users").child("profile").child("account")
                        .child(mUser.getUid()).child("dob")
                        .setValue(dob).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent mIntent = new Intent(this, HomeActivity.class);
                                startActivity(mIntent);
                                Bungee.slideLeft(this);
                            }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfUserExistsInDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfUserExistsInDb();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkIfUserExistsInDb();
    }

    private void checkIfUserExistsInDb() {
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            // start from the create flow
            mainViewModel.setIsFirstTimeUser(true);
        } else {
            // check to see if user has saved name
            mProfileRef.child("users").child("profile").child("account")
                    .child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String publicName = dataSnapshot.child("publicName").getValue().toString();

                    if (publicName.isEmpty()) {
                        mainViewModel.setIsFirstTimeUser(true);
                    } else {

                        // check for Gender

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
