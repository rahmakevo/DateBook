package com.example.datebook.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datebook.R;
import com.example.datebook.adapter.MessageRecyclerViewAdapter;
import com.example.datebook.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import spencerstudios.com.bungeelib.Bungee;

public class MessageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mRecipientDb;
    private DatabaseReference mRecipientRef;
    private String recipient_id = null;
    private RecyclerView mMessageList;
    private MessageRecyclerViewAdapter adapter;
    private List<MessageModel> messageModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recipient_id = getIntent().getStringExtra("recipient_id");
        readMessages(recipient_id);

        mAuth = FirebaseAuth.getInstance();
        mRecipientDb = FirebaseDatabase.getInstance();
        mRecipientRef = mRecipientDb.getReference();

        CircleImageView mImageRecipientProfile = findViewById(R.id.imageViewUserMessage);
        TextView mTextProfileName = findViewById(R.id.textViewNameMessage);
        TextView mTextOnline = findViewById(R.id.textViewChatsOnline);

        mRecipientRef
                .child("users").child("profile").child(recipient_id)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get()
                        .load(snapshot.child("publicThumbnail").getValue().toString())
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(mImageRecipientProfile);
                mTextProfileName.setText(snapshot.child("publicName").getValue().toString());

                if (snapshot.hasChild("userPresence")) {
                    if (snapshot.child("userPresence").getValue().toString().equals("true")) {
                        mTextOnline.setText("Online");
                    } else {
                        mTextOnline.setText("Offline");
                    }
                } else {
                    mTextOnline.setText("Offline");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView mImageBack = findViewById(R.id.imageViewBackMessage);
        mImageBack.setOnClickListener(view -> {
            Intent mIntentBack = new Intent(MessageActivity.this, HomeActivity.class);
            startActivity(mIntentBack);
            Bungee.slideRight(this);
        });

        mMessageList = findViewById(R.id.list_view_message);
        mMessageList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(false);
        mMessageList.setLayoutManager(layoutManager);

        View rootView = findViewById(R.id.root_view);
        ImageView mEmoji = findViewById(R.id.imageViewMood);
        EmojiconEditText mMessageText = findViewById(R.id.editTextMessage);

        mEmoji.setOnClickListener(view -> {
            EmojIconActions emojIcon = new EmojIconActions(
                    this,
                    rootView,
                    mMessageText,
                    mEmoji
            );
            emojIcon.ShowEmojIcon();
            emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);

            InputMethodManager keyboard = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(mMessageText, 0);

        });

        ImageView mImageSend = findViewById(R.id.imageViewSendMessage);
        mImageSend.setOnClickListener(view -> {
            String mMessage = mMessageText.getText().toString();

            if (mMessage.isEmpty()) {
                mMessageText.setText("");
                mMessageText.setError("Message cannot be sent empty!");
            } else {

                mMessageText.setText("");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String message_time = sdf.format(cal.getTime());

                HashMap<String, String> mMessageMap = new HashMap<>();
                mMessageMap.put("from", mAuth.getCurrentUser().getUid());
                mMessageMap.put("to", recipient_id);
                mMessageMap.put("message", mMessage);
                mMessageMap.put("time", message_time);

                // prepare sent thread
                mRecipientRef
                        .child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                        .child(recipient_id).child("messages").child("thread").push().setValue(mMessageMap)
                        .addOnSuccessListener(taskSent -> {
                            // prepare receive thread
                            mRecipientRef
                                    .child("users").child("chat").child(recipient_id).child(mAuth.getCurrentUser().getUid())
                                    .child("messages").child("thread").push().setValue(mMessageMap)
                                    .addOnSuccessListener(taskReceived -> {
                                        // prepare notification thread
                                        mRecipientRef
                                                .child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                                                .child(recipient_id).child("messages").child("thread").child("notifications")
                                                .push().setValue(mMessageMap).addOnSuccessListener(taskNotify -> {
                                            adapter.notifyDataSetChanged();
                                        });
                                    });
                        });
            }
        });

        ImageView mVideoCall = findViewById(R.id.startVideoCallUserMessage);
        mVideoCall.setOnClickListener(view -> {
            mRecipientRef
                    .child("users").child("calls").child(mAuth.getCurrentUser().getUid())
                    .child("initiateCall").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(recipient_id)) {
                        Intent mIntent = new Intent(MessageActivity.this, CallingActivity.class);
                        mIntent.putExtra("caller_id", mAuth.getCurrentUser().getUid());
                        mIntent.putExtra("recipient_id", recipient_id);
                        startActivity(mIntent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }

    private void readMessages(String recipientId) {
        messageModels = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        DatabaseReference mMessagesRef = FirebaseDatabase.getInstance().getReference();
        mMessagesRef
                .child("users").child("chat").child(Objects.requireNonNull(mUser).getUid())
                .child(Objects.requireNonNull(recipientId)).child("messages").child("thread")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            if (model.getFrom() != null) {
                                messageModels.add(model);
                            }
                        }

                        adapter = new MessageRecyclerViewAdapter(messageModels, recipientId);
                        mMessageList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
