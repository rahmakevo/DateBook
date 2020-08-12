package com.example.datebook.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.datebook.R;
import com.example.datebook.fragments.CreateAccountOne;
import com.example.datebook.fragments.CreateAccountTwo;
import com.example.datebook.model.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationFlowActivity extends AppCompatActivity {
    protected MainViewModel mainViewModel;
    FrameLayout mFragmentLayout;

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mProfileDb;
    protected DatabaseReference mProfileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_flow);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mFragmentLayout = findViewById(R.id.auth_fragment_container);
        mAuth = FirebaseAuth.getInstance();
        mProfileDb = FirebaseDatabase.getInstance();
        mProfileRef = mProfileDb.getReference();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfUserExistsInDb();
    }

    private void checkIfUserExistsInDb() {
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            // start from the create flow
            mainViewModel.setIsFirstTimeUser(true);
        }
    }
}
