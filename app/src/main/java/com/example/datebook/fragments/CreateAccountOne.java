package com.example.datebook.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.datebook.MainActivity;
import com.example.datebook.R;
import com.example.datebook.model.MainViewModel;

public class CreateAccountOne extends Fragment {
    private EditText mProfileNameText;
    private ProgressBar progressBar;
    protected MainViewModel mainViewModel;
    public CreateAccountOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_create_account_one, container, false);

        progressBar = mView.findViewById(R.id.progress);

        ImageView mBackPressed = mView.findViewById(R.id.createAccountOneBackButton);
        mBackPressed.setOnClickListener(v -> {
            Intent intent = new Intent(mView.getContext(), MainActivity.class);
            startActivity(intent);
        });

        mProfileNameText = mView.findViewById(R.id.textInputLayoutQuick);
        Button btnProfileName = mView.findViewById(R.id.buttonProceedName);
        btnProfileName.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String mProfileName = mProfileNameText.getText().toString();

            if (mProfileName.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                mProfileNameText.setError("Dear Customer, Profile Name cannot be empty");
            } else {
                // validate according to policy standard
                // save name to db in case user does not finish setup
                progressBar.setVisibility(View.GONE);
                mainViewModel.setPublicName(mProfileName);
            }
        });

        return mView;
    }
}
