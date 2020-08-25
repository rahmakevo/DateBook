package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.MatchModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {
    private List<MatchModel> model;
    private Context context;
    private Boolean liked = false;
    private int mCount = 0;

    public MatchRecyclerViewAdapter(List<MatchModel> model) { this.model = model; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_match_item,
                parent,
                false
        );
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel matchModel = model.get(position);
        holder.mTextUserName.setText(matchModel.public_name);

        Picasso.get().load(matchModel.thumb_profile).into(holder.mImageAvatar);
        Picasso.get().load(matchModel.thumb_profile).into(holder.mImageMatchMain);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImageAvatar;
        private CircleImageView mImageAvatarOnline;
        private ImageView mImageMatchMain;
        private ImageView mImageMatchLike;
        private ImageView mImageMatchDisLike;
        private ImageView mImageVideoCall;
        private ImageView mImageChat;
        private ImageView mImageShareProfile;
        private TextView mTextUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageAvatar = itemView.findViewById(R.id.imageViewAvatar);
            mImageAvatarOnline = itemView.findViewById(R.id.imageViewOnline);
            mImageMatchMain = itemView.findViewById(R.id.imageUserBackground);
            mImageMatchLike = itemView.findViewById(R.id.imageViewShowLove);
            mImageMatchDisLike = itemView.findViewById(R.id.imageViewShowHate);
            mImageVideoCall = itemView.findViewById(R.id.imageViewVideoCall);
            mImageChat = itemView.findViewById(R.id.imageViewMessage);
            mImageShareProfile = itemView.findViewById(R.id.imageViewShare);
            mTextUserName = itemView.findViewById(R.id.textViewUserName);
        }
    }
}
