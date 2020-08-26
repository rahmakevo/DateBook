package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.InitiateChatModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private List<InitiateChatModel> model;
    private Context context;

    private FirebaseDatabase mChatDb;
    private DatabaseReference mChatRef;

    public ChatRecyclerViewAdapter(List<InitiateChatModel> model) { this.model = model; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_chat_item,
                parent,
                false
        );
        mChatDb = FirebaseDatabase.getInstance();
        mChatRef = mChatDb.getReference();
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InitiateChatModel initiateChatModel = model.get(position);

        // get recipient details
        mChatRef.child("users").child("profile").child(initiateChatModel.recipient_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.mChatPublicName.setText(snapshot.child("publicName").getValue().toString());

                        Picasso.get().load(snapshot.child("publicThumbnail").getValue().toString())
                                .placeholder(R.drawable.ic_account_circle_black_24dp)
                                .into(holder.mImageChatAvatar);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImageChatAvatar;
        private TextView mChatPublicName;
        private TextView mCurrentSentMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageChatAvatar = itemView.findViewById(R.id.imageMessageProfile);
            mChatPublicName = itemView.findViewById(R.id.textMessageUserName);
            mCurrentSentMessage = itemView.findViewById(R.id.textMessageContext);
        }
    }
}
