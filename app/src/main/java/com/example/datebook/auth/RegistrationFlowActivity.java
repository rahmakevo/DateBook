package com.example.datebook.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.datebook.R;
import com.example.datebook.fragments.CreateAccountOne;
import com.example.datebook.model.MainViewModel;

public class RegistrationFlowActivity extends AppCompatActivity {
    protected MainViewModel mainViewModel;
    FrameLayout mFragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_flow);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mFragmentLayout = findViewById(R.id.auth_fragment_container);
    }
}
