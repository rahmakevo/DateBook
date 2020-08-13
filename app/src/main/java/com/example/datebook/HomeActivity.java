package com.example.datebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.datebook.adapter.ViewPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    protected ViewPagerFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TabLayout mHomeLayout = findViewById(R.id.tabLayoutHome);
        ViewPager viewPager2 = findViewById(R.id.viewPagerHome);

        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(adapter);
        mHomeLayout.setupWithViewPager(viewPager2);
    }
}
