package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.MatchModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMatchesRecyclerAdapter extends RecyclerView.Adapter<NewMatchesRecyclerAdapter.ViewHolder> {
    private List<MatchModel> matchModel;
    private Context context;

    public NewMatchesRecyclerAdapter(List<MatchModel> matchModel) {
        this.matchModel = matchModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_match_layout,
                parent,
                false
        );
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel model = matchModel.get(position);
        holder.mTextProfile.setText(model.public_name);

        Picasso.get().load(model.thumb_profile).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.mImageProfile);
    }

    @Override
    public int getItemCount() {
        return matchModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImageProfile;
        TextView mTextProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageProfile = itemView.findViewById(R.id.imageViewMatchProfile);
            mTextProfile = itemView.findViewById(R.id.textViewMatchProfileName);
        }
    }
}
