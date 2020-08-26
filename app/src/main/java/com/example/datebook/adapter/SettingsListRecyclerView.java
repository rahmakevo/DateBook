package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.SettingsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsListRecyclerView extends RecyclerView.Adapter<SettingsListRecyclerView.ViewHolder> {
    private List<SettingsModel> modelList;
    private Context context;

    public SettingsListRecyclerView(List<SettingsModel> modelList) { this.modelList = modelList; }

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
