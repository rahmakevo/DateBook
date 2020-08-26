package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.MatchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {
    private List<MatchModel> model;
    private Context context;
    private Boolean liked = false;
    private int mCount = 0;

    private FirebaseAuth mAuth;
    private DatabaseReference mChatInitiateRef;
    private FirebaseDatabase mChatInitiateDb;

    public MatchRecyclerViewAdapter(List<MatchModel> model) { this.model = model; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_match_item,
                parent,
                false
        );
        mAuth = FirebaseAuth.getInstance();
        mChatInitiateDb = FirebaseDatabase.getInstance();
        mChatInitiateRef = mChatInitiateDb.getReference();
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchModel matchModel = model.get(position);
        holder.mTextUserName.setText(matchModel.public_name);

        Picasso.get().load(matchModel.thumb_profile).into(holder.mImageAvatar);
        Picasso.get().load(matchModel.thumb_profile).into(holder.mImageMatchMain);

        holder.mImageChat.setOnClickListener(v -> {

            mChatInitiateRef.child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                    .child("initiateChat").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // check if child exist
                    if (!snapshot.hasChild(matchModel.user_id)) {
                        // create initiate chat Map
                        Date objDate = new Date();
                        HashMap<String, String> mChatInitiateMap = new HashMap<>();
                        mChatInitiateMap.put("recipient_id", matchModel.user_id);
                        mChatInitiateMap.put("sender_id", mAuth.getCurrentUser().getUid());
                        mChatInitiateMap.put("date", String.valueOf(objDate));

                        mChatInitiateRef.child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                                .child("initiateChat").child(matchModel.user_id).setValue(mChatInitiateMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // open page
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

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
