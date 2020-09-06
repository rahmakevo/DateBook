package com.example.datebook.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datebook.R;
import com.example.datebook.fragments.BottomSheetNameFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class ProfileSettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mProfileDb;
    private DatabaseReference mProfileRef;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private ProgressBar progressBar;

    private ImageView mImageProfileMain, mImageProfileTwo, mImageProfileThree,
                      mImageProfileFour, ImageProfileFive, mImageProfileSix;
    private CircleImageView mImageThumbnail;
    private ImageView mImageChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        progressBar = findViewById(R.id.progressProfileSettings);
        progressBar.setVisibility(View.VISIBLE);

        ImageView mImageBack = findViewById(R.id.imageBackSettingsProfilePref);
        mImageBack.setOnClickListener(view -> {
            Intent mIntentBack = new Intent(this, SettingsActivity.class);
            startActivity(mIntentBack);
            Bungee.slideRight(this);
        });

        mAuth = FirebaseAuth.getInstance();
        mProfileDb = FirebaseDatabase.getInstance();
        mProfileRef = mProfileDb.getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        TextView mTextName = findViewById(R.id.textViewProfileNameSettings);
        TextView mTextStatus = findViewById(R.id.textViewProfileStatusSettings);
        mImageThumbnail = findViewById(R.id.circleImageViewThumbProfile);
        mProfileRef
                .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mTextName.setText(snapshot.child("publicName").getValue().toString());
                        mTextStatus.setText(snapshot.child("status").getValue().toString());

                        Picasso.get().load(snapshot.child("publicThumbnail").getValue().toString())
                                .into(mImageThumbnail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mImageProfileMain = findViewById(R.id.imageViewProfileProfileOne);
        StorageReference mStorageRefMain = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("main.jpg");
        mStorageRefMain.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(mImageProfileMain);
            }
        });

        mImageProfileTwo = findViewById(R.id.imageViewProfilePhotosTwo);
        StorageReference mStorageRefTwo = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("two.jpg");
        mStorageRefTwo.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(mImageProfileTwo);
            }
        });

        mImageProfileThree = findViewById(R.id.imageViewProfilePhotosThree);
        StorageReference mStorageRefThree = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("three.jpg");
        mStorageRefThree.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(mImageProfileThree);
            }
        });

        mImageProfileFour = findViewById(R.id.imageViewProfilePhotosFour);
        StorageReference mStorageRefFour = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("four.jpg");
        mStorageRefFour.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(mImageProfileFour);
            }
        });

        ImageProfileFive = findViewById(R.id.imageViewProfilePhotosFive);
        StorageReference mStorageRefFive = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("five.jpg");
        mStorageRefFive.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(ImageProfileFive);
            }
        });

        mImageProfileSix = findViewById(R.id.imageViewProfilePhotosSix);
        StorageReference mStorageRefSix = FirebaseStorage.getInstance().getReference()
                .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("six.jpg");
        mStorageRefSix.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Picasso.get().load(task.getResult()).into(mImageProfileSix);
            }
        });

        progressBar.setVisibility(View.GONE);

        ImageView mImageChangeProfile = findViewById(R.id.imageViewChangeProfileThumbnail);
        mImageChangeProfile.setOnClickListener(view -> {
            chooseImage(0);
        });

        CircleImageView mImageMainAdd = findViewById(R.id.ImageViewProfilePhotosAddOne);
        mImageMainAdd.setOnClickListener(view -> {
            chooseImage(1);
        });

        CircleImageView mImageTwoAdd = findViewById(R.id.ImageViewProfilePhotosAddTwo);
        mImageTwoAdd.setOnClickListener(view -> {
            chooseImage(2);
        });

        CircleImageView mImageAddThree = findViewById(R.id.ImageViewProfilePhotosAddThree);
        mImageAddThree.setOnClickListener(view -> {
            chooseImage(3);
        });

        CircleImageView mImageAddFour = findViewById(R.id.ImageViewProfilePhotosAddFour);
        mImageAddFour.setOnClickListener(view -> {
            chooseImage(4);
        });

        CircleImageView mImageAddFive = findViewById(R.id.ImageViewProfilePhotoAddFive);
        mImageAddFive.setOnClickListener(view -> {
            chooseImage(5);
        });

        CircleImageView mImageAddSix = findViewById(R.id.ImageViewProfilePhotosAddSix);
        mImageAddSix.setOnClickListener(view -> {
            chooseImage(6);
        });

        mImageChangeName = findViewById(R.id.imageViewEdiTextName);
        mImageChangeName.setOnClickListener(view -> {
            BottomSheetNameFragment fragment = new BottomSheetNameFragment("Change your Public Name");
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });

        ImageView mChangeBio = findViewById(R.id.imageViewEdiTextStatus);
        mChangeBio.setOnClickListener(view -> {
            BottomSheetNameFragment fragment = new BottomSheetNameFragment("Change your Bio");
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntentBack = new Intent(this, SettingsActivity.class);
        startActivity(mIntentBack);
        Bungee.slideRight(this);
    }

    private void chooseImage(int position) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case 0:
                    Uri mImageViewPath = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPath);
                        mImageThumbnail.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/thumbnail.jpg")
                                .putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                StorageReference mStorageRefThumbnail = FirebaseStorage.getInstance().getReference()
                                        .child("users").child("gallery").child(mAuth.getCurrentUser().getUid()).child("thumbnail.jpg");
                                mStorageRefThumbnail.getDownloadUrl().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                mProfileRef.child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                                        .child("publicThumbnail").setValue(task.getResult())
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                                                            }});
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                                            }});
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Uri mImageViewPathOne = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathOne);
                        mImageProfileMain.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/main.jpg")
                                .putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Uri mImageViewPathTwo = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathTwo);
                        mImageProfileTwo.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/two.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Uri mImageViewPathThree = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathThree);
                        mImageProfileThree.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/three.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    Uri mImageViewPathFour = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathFour);
                        mImageProfileFour.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/four.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    Uri mImageViewPathFive = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathFive);
                        ImageProfileFive.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/five.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    Uri mImageViewPathSix = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathSix);
                        mImageProfileSix.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/six.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Upload was successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(this, "Error uploading. Kindly try again to proceed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
