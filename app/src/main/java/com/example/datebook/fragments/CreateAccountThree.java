package com.example.datebook.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

import com.example.datebook.R;
import com.example.datebook.model.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccountThree extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mBirthDb;
    private DatabaseReference mBirthRef;

    protected MainViewModel mainViewModel;

    public CreateAccountThree() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentManager mManager = getActivity().getSupportFragmentManager();
                CreateAccountThree mFragment = new CreateAccountThree();
                mManager.beginTransaction()
                        .replace(R.id.auth_fragment_container, mFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_create_account_three, container, false);

        mAuth = FirebaseAuth.getInstance();
        mBirthDb = FirebaseDatabase.getInstance();
        mBirthRef = mBirthDb.getReference();

        DatePicker mDatePicker = mView.findViewById(R.id.datePicker);
        Button btnDatePicker = mView.findViewById(R.id.buttonProceedDob);
        btnDatePicker.setOnClickListener(view -> {
            int mDay = mDatePicker.getDayOfMonth();
            int mMonth = mDatePicker.getMonth();
            int mYear = mDatePicker.getYear();

            if (isAgeValid(mYear)) {
                String mDob = mDay + "/" + mMonth + "/" + mYear;

                mainViewModel.setDobSelected(mDob);
            }

        });

        ImageView mImageBack = mView.findViewById(R.id.createAccountThreeBackButton);
        mImageBack.setOnClickListener(view -> {
            FragmentManager mManager = getActivity().getSupportFragmentManager();
            CreateAccountThree mFragment = new CreateAccountThree();
            mManager.beginTransaction()
                    .replace(R.id.auth_fragment_container, mFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        return mView;
    }

    private boolean isAgeValid(int mYear) {
        // check for age above 13 years
        // above age
        return mYear <= 2007;
    }
}
