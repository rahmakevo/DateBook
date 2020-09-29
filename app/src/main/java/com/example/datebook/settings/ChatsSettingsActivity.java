package com.example.datebook.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.datebook.R;
import com.example.datebook.adapter.SettingsListRecyclerView;
import com.example.datebook.model.SettingsModel;

import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class ChatsSettingsActivity extends AppCompatActivity {
    private List<SettingsModel> modelList = new ArrayList<>();
    private SettingsListRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_settings);

        RecyclerView recyclerView = findViewById(R.id.list_chats_settings);
        adapter = new SettingsListRecyclerView(modelList, getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        recyclerView.setAdapter(adapter);
        prepareData();

        ImageView imageBack = findViewById(R.id.imageBackSettingsChatsPref);
        imageBack.setOnClickListener(view -> {
            Intent mIntent = new Intent(this, SettingsActivity.class);
            startActivity(mIntent);
            Bungee.slideRight(this);
        });
    }

    private void prepareData() {
        SettingsModel model = new SettingsModel("Delete all conversations", R.drawable.ic_delete_black_24dp);
        modelList.add(model);

        model = new SettingsModel("Clear all conversations", R.drawable.ic_clear_black_24dp);
        modelList.add(model);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(this, SettingsActivity.class);
        startActivity(mIntent);
        Bungee.slideRight(this);
    }
}
