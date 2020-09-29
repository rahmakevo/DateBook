package com.example.datebook.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> {
    public static final int message_sender = 0;
    public static final int message_receiver = 1;
    public static final int no_messages_yet = 2;

    private List<MessageModel> model;
    private String recipientId;

    public MessageRecyclerViewAdapter(List<MessageModel> model, String recipientId) {
        this.model = model;
        this.recipientId = recipientId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == message_receiver) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel = model.get(position);

        holder.mText.setText(messageModel.getMessage());
        holder.mTime.setText(messageModel.getTime());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public TextView mTime;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.message_text);
            mTime = itemView.findViewById(R.id.message_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = model.get(position);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUserId = mAuth.getCurrentUser();

        if (messageModel.getFrom().equals(Objects.requireNonNull(mUserId).getUid())) {
            return message_sender;
        } else {
            return message_receiver;
        }
    }
}
