package com.example.datebook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.settings.AccountSettingsActivity;
import com.example.datebook.settings.ChatsSettingsActivity;
import com.example.datebook.settings.NotificationsSettingsActivity;
import com.example.datebook.settings.PreferencesMatchesActivity;
import com.example.datebook.model.SettingsModel;
import com.example.datebook.settings.PrivacySettingsActivity;
import com.example.datebook.settings.ProfileSettingsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class SettingsListRecyclerView extends RecyclerView.Adapter<SettingsListRecyclerView.ViewHolder> {
    private List<SettingsModel> modelList;
    private Context context;
    public FragmentManager manager;

    public SettingsListRecyclerView(List<SettingsModel> modelList, FragmentManager manager) {
        this.modelList = modelList;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_settings_item,
                parent,
                false
        );
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsModel model = modelList.get(position);

        holder.mTextSettingsName.setText(model.name);
        holder.mImageSettingsIcon.setImageResource(model.icon);

        holder.itemView.setOnClickListener(view -> {
            switch (model.name) {
                case "Match Preferences":
                    Intent mIntent = new Intent(context.getApplicationContext(), PreferencesMatchesActivity.class);
                    context.startActivity(mIntent);
                    Bungee.slideLeft(context);
                    break;
                case "Chats":
                    Intent mIntentChats = new Intent(context.getApplicationContext(), ChatsSettingsActivity.class);
                    context.startActivity(mIntentChats);
                    Bungee.slideLeft(context);
                    break;
                case "Account":
                    Intent mIntentAccount =  new Intent(context.getApplicationContext(), AccountSettingsActivity.class);
                    context.startActivity(mIntentAccount);
                    Bungee.slideLeft(context);
                    break;
                case "Notifications":
                    Intent mIntentNotify = new Intent(context.getApplicationContext(), NotificationsSettingsActivity.class);
                    context.startActivity(mIntentNotify);
                    Bungee.slideLeft(context);
                    break;
                case "Privacy":
                    Intent mIntentPrivacy = new Intent(context.getApplicationContext(), PrivacySettingsActivity.class);
                    context.startActivity(mIntentPrivacy);
                    Bungee.slideLeft(context);
                    break;
                case "Invite a friend":
                    Intent mShareIntent = new Intent(Intent.ACTION_SEND);
                    mShareIntent.setType("text/plain");
                    context.startActivity(mShareIntent);
                    break;
                case "Profile":
                    Intent mIntentProfile = new Intent(context.getApplicationContext(), ProfileSettingsActivity.class);
                    context.startActivity(mIntentProfile);
                    Bungee.slideLeft(context);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImageSettingsIcon;
        private TextView mTextSettingsName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageSettingsIcon = itemView.findViewById(R.id.imageViewIconSettings);
            mTextSettingsName = itemView.findViewById(R.id.textViewSettingsName);
        }
    }
}
