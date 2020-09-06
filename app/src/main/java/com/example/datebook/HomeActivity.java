package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datebook.adapter.ViewPagerFragmentAdapter;
import com.example.datebook.settings.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class HomeActivity extends AppCompatActivity {
    protected ViewPagerFragmentAdapter adapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        TabLayout mHomeLayout = findViewById(R.id.tabLayoutHome);
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Home"));
        mHomeLayout.addTab(mHomeLayout.newTab().setText("Calls"));
        ViewPager viewPager2 = findViewById(R.id.viewPagerHome);

        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(adapter);
        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mHomeLayout));
        mHomeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Spinner spinner = findViewById(R.id.spinner_language_home);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.language_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter2);

        ImageView mImageMore = findViewById(R.id.imageViewMore);
        mImageMore.setOnClickListener(view -> {
            Intent mIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideLeft(this);
        });

        Geocoder geocoder = new Geocoder(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if(addresses != null && addresses.size() > 0) {
                                String countryName = addresses.get(0).getCountryName();

                                Log.v("countryName :", countryName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
