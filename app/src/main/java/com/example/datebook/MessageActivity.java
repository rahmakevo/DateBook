package com.example.datebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import spencerstudios.com.bungeelib.Bungee;

public class MessageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mRecipientDb;
    private DatabaseReference mRecipientRef;
    private String recipient_id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recipient_id = getIntent().getStringExtra("recipient_id");

        mAuth = FirebaseAuth.getInstance();
        mRecipientDb = FirebaseDatabase.getInstance();
        mRecipientRef = mRecipientDb.getReference();

        CircleImageView mImageRecipientProfile = findViewById(R.id.imageViewUserMessage);
        TextView mTextProfileName = findViewById(R.id.textViewNameMessage);

        if (!recipient_id.equals(null)) {
            mRecipientRef.child("users").child("profile").child(recipient_id);
            mRecipientRef.keepSynced(true);
            mRecipientRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Picasso.get()
                                    .load(snapshot.child("publicThumbnail").getValue().toString())
                                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                                    .into(mImageRecipientProfile);
                            mTextProfileName.setText(snapshot.child("publicName").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        ImageView mImageBack = findViewById(R.id.imageViewBackMessage);
        mImageBack.setOnClickListener(view -> {
            Intent mIntentBack = new Intent(MessageActivity.this, HomeActivity.class);
            startActivity(mIntentBack);
            Bungee.slideRight(this);
        });

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
                mMessageText.setError("Message cannot be sent empty!");
            } else {

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String message_time = sdf.format(cal.getTime());

                HashMap<String, String> mMessageMap = new HashMap<>();
                mMessageMap.put("from", mAuth.getCurrentUser().getUid());
                mMessageMap.put("to", recipient_id);
                mMessageMap.put("message", mMessage);
                mMessageMap.put("time", message_time);

                // prepare sent messages
                mRecipientRef.child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                        .child("sent").child(recipient_id).push().setValue(mMessageMap)
                        .addOnCompleteListener(taskSent -> {
                           if (taskSent.isSuccessful()) {

                               // prepare received messages
                               mRecipientRef.child("users").child("chat").child(recipient_id)
                                       .child("received").child(mAuth.getCurrentUser().getUid())
                                       .push().setValue(mMessageMap)
                                       .addOnCompleteListener(taskReceived -> {
                                          if (taskReceived.isSuccessful()) {

                                          }
                                       });
                           }
                        });


            }
        });

    }
}
