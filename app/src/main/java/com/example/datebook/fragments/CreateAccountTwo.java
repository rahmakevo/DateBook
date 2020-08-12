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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.datebook.R;
import com.example.datebook.model.MainViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccountTwo extends Fragment {

    private CircleImageView maleAvatar;
    private CircleImageView malePickAvatar;

    private CircleImageView femaleAvatar;
    private CircleImageView femalePickAvatar;

    private Boolean isMaleSelected = false;
    private  Boolean isFemaleSelected = false;

    private ProgressBar progressBar;
    protected MainViewModel mainViewModel;

    public CreateAccountTwo() {
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
                CreateAccountOne mFragment = new CreateAccountOne();
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
        View mView = inflater.inflate(R.layout.fragment_create_account_two, container, false);

        maleAvatar = mView.findViewById(R.id.avatar_image_male);
        malePickAvatar = mView.findViewById(R.id.icon_pick_male);

        femaleAvatar = mView.findViewById(R.id.avatar_image_female);
        femalePickAvatar = mView.findViewById(R.id.icon_pick_female);

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

        progressBar = mView.findViewById(R.id.progress);
        Button btnProceed = mView.findViewById(R.id.buttonProceedGender);
        btnProceed.setOnClickListener(view -> {

            if (!isMaleSelected && !isFemaleSelected) {
                Toast.makeText(getActivity(), R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
            } else {

                if (isMaleSelected) {
                    mainViewModel.setSelectedGender("male");
                } else if (isFemaleSelected) {
                    mainViewModel.setSelectedGender("female");
                }

            }

        });

        ImageView mImageBack = mView.findViewById(R.id.createAccountTwoBackButton);
        mImageBack.setOnClickListener(view -> {
            FragmentManager mManager = getActivity().getSupportFragmentManager();
            CreateAccountOne mFragment = new CreateAccountOne();
            mManager.beginTransaction()
                    .replace(R.id.auth_fragment_container, mFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        return mView;
    }
}
