package com.example.datebook.auth;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datebook.R;
import com.example.datebook.util.ApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class CreateAccountTwo extends AppCompatActivity {

    private CircleImageView maleAvatar;
    private CircleImageView malePickAvatar;

    private CircleImageView femaleAvatar;
    private CircleImageView femalePickAvatar;
    private String mPublicName;

    private Boolean isMaleSelected = false;
    private  Boolean isFemaleSelected = false;

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase mProfileDb;
    protected DatabaseReference mProfileRef;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public CreateAccountTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_create_account_two);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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

        FirebaseUser mUser = mAuth.getCurrentUser();
        mProfileRef.child("users").child("profile").child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            mPublicName = snapshot.child("publicName").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Button btnProceed = findViewById(R.id.buttonProceedGender);
        btnProceed.setOnClickListener(view -> {

            if (!isMaleSelected && !isFemaleSelected) {
                Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
            } else {

                if (isMaleSelected) {

                    mProfileRef.child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("male").addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Date objDate = new Date();

                                    HashMap<String, String> mGenderMap = new HashMap<>();
                                    mGenderMap.put("date", objDate.toString());
                                    mGenderMap.put("public_name", mPublicName);
                                    mGenderMap.put("thumb_profile", String.valueOf(mUser.getPhotoUrl()));
                                    mGenderMap.put("user_id", mUser.getUid());
                                    mProfileRef.child("users").child("matches").child("male").child(mUser.getUid())
                                            .setValue(mGenderMap).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Intent mIntent = new Intent(this, CreateAccountThree.class);
                                            startActivity(mIntent);
                                            Bungee.slideLeft(this);
                                        } else {
                                            Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                }
                            });

                } else if (isFemaleSelected) {
                    mProfileRef
                            .child("users").child("profile").child(mUser.getUid()).child("gender")
                            .setValue("female").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Date objDate = new Date();

                            HashMap<String, String> mGenderMap = new HashMap<>();
                            mGenderMap.put("date", objDate.toString());
                            mGenderMap.put("public_name", mPublicName);
                            mGenderMap.put("thumb_profile", String.valueOf(mUser.getPhotoUrl()));
                            mGenderMap.put("user_id", mUser.getUid());
                            mProfileRef.child("users").child("matches").child("female").child(mUser.getUid())
                                    .setValue(mGenderMap).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Intent mIntent = new Intent(this, CreateAccountThree.class);
                                    startActivity(mIntent);
                                    Bungee.slideLeft(this);
                                } else {
                                    Toast.makeText(this, R.string.not_gender_selected, Toast.LENGTH_SHORT).show();
                                }
                            });
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

        fusedLocationProviderClient
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        getAddress(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                    } else {
                        Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAddress(String valueOf, String valueOf1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getAddressResponse().execute(valueOf, valueOf1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private class getAddressResponse extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String lat = strings[0];
            String lon = strings[1];

            Log.v("lat", lat+lon);

            ApiService apiService = new ApiService();
            String response = apiService.AddressLookUp(lat, lon);

            try {

                JSONObject responseObject = new JSONObject(response);
                String status = responseObject.getString("status");

                if (!status.equals("REQUEST_DENIED") && status.equals("OK")) {
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i=0; i<results.length(); i++) {
                        JSONObject resultsObject = results.getJSONObject(i);
                        String formattedAddress = resultsObject.getString("formatted_address");

                        String[] splitAddress = formattedAddress.split(",");
                        ArrayList<String> listAddress = new ArrayList<>(Arrays.asList(splitAddress));
                        String mCountry = listAddress.get(listAddress.size() - 1);
                        String mLocality = listAddress.get(listAddress.size() - 3);

                        Date date = new Date();

                        HashMap<String, String> mAddressMap = new HashMap<>();
                        mAddressMap.put("country", mCountry);
                        mAddressMap.put("locality", mLocality);
                        mAddressMap.put("lat", lat);
                        mAddressMap.put("long", lon);
                        mAddressMap.put("address_lookup_time", String.valueOf(date));

                        if (isMaleSelected || isFemaleSelected) {
                            if (isMaleSelected) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                FirebaseDatabase mAddressDb = FirebaseDatabase.getInstance();
                                DatabaseReference mAddressRef = mAddressDb.getReference();
                                mAddressRef
                                        .child("users").child("matches").child("male")
                                        .child(mUser.getUid()).child("addressLookUp")
                                        .setValue(mAddressMap).addOnSuccessListener(success -> {});
                            } else if (isFemaleSelected) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                FirebaseDatabase mAddressDb = FirebaseDatabase.getInstance();
                                DatabaseReference mAddressRef = mAddressDb.getReference();
                                mAddressRef
                                        .child("users").child("matches").child("female")
                                        .child(mUser.getUid()).child("addressLookUp")
                                        .setValue(mAddressMap).addOnSuccessListener(success -> {});
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.v("response", response);

            return null;
        }
    }
}
