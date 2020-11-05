package com.example.datebook.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datebook.R;

public class CallingActivity extends AppCompatActivity {
    private Boolean mSpeakerMic = false;
    private Boolean mVideoHide = false;
    private Boolean mAudioHide = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        ImageView imageSpeaker = findViewById(R.id.imageViewSpeaker);
        imageSpeaker.setOnClickListener(view -> {
            if (mSpeakerMic) {
                mSpeakerMic = false;
                imageSpeaker.setImageResource(R.drawable.baseline_volume_up_white_18dp);
            } else  {
                mSpeakerMic = true;
                imageSpeaker.setImageResource(R.drawable.baseline_volume_off_white_24dp);
            }
        });

        ImageView imageVideoCall = findViewById(R.id.imageViewVideoStream);
        imageVideoCall.setOnClickListener(view -> {
            if (mVideoHide) {
                mVideoHide = false;
                imageVideoCall.setImageResource(R.drawable.baseline_videocam_white_24dp);
            } else {
                mVideoHide = true;
                imageVideoCall.setImageResource(R.drawable.baseline_videocam_off_white_24dp);
            }
        });

        ImageView imageAudio = findViewById(R.id.imageViewAudioMute);
        imageAudio.setOnClickListener(view -> {
            if (mAudioHide) {
                mAudioHide = false;
                imageAudio.setImageResource(R.drawable.baseline_mic_white_24dp);

            } else  {
                mAudioHide = true;
                imageAudio.setImageResource(R.drawable.baseline_mic_off_white_24dp);
            }
        });

        ImageView imageCutCall = findViewById(R.id.imageViewHangup);
        imageCutCall.setOnClickListener(view -> {
            finish();
        });

    }
}
