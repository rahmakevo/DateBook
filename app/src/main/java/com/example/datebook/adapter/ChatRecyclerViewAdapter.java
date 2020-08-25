package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datebook.R;
import com.example.datebook.model.ChatModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private List<ChatModel> model;
    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_chat_item,
                parent,
                false
        );
        context = mView.getContext();
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel chatModel = model.get(position);

        holder.mChatPublicName.setText(chatModel.public_name);
        holder.mCurrentSentMessage.setText("Hey there, I'm using gaga");

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
