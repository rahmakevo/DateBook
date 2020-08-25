package com.example.datebook.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.datebook.HomeActivity;
import com.example.datebook.MainActivity;
import com.example.datebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class CreateAccountFour extends AppCompatActivity {

    private ImageView
            mImageViewProfileOne, mImageViewProfileTwo, mImageViewProfileThree,
            mImageViewProfileFour, mImageViewProfileFive, mImageViewProfileSix;

    private Boolean mPathOneSet = false;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_four);

        progressBar = findViewById(R.id.progressCreateAccountFour);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        CircleImageView mImageViewPhotosOne = findViewById(R.id.ImageViewPhotosOne);
        mImageViewPhotosOne.setOnClickListener(viewOne -> { chooseImage(0); });

        CircleImageView mImageViewPhotosTwo = findViewById(R.id.ImageViewPhotosTwo);
        mImageViewPhotosTwo.setOnClickListener(viewTwo -> { chooseImage(1); });

        CircleImageView mImageViewPhotosThree = findViewById(R.id.ImageViewPhotosThree);
        mImageViewPhotosThree.setOnClickListener(viewThree -> { chooseImage(2); });

        CircleImageView mImageViewPhotosFour = findViewById(R.id.ImageViewPhotosFour);
        mImageViewPhotosFour.setOnClickListener(viewFour -> { chooseImage(3); });

        CircleImageView mImageViewPhotoFive = findViewById(R.id.ImageViewPhotoFive);
        mImageViewPhotoFive.setOnClickListener(viewFive -> { chooseImage(4); });

        CircleImageView mImageViewPhotosSix = findViewById(R.id.ImageViewPhotosSix);
        mImageViewPhotosSix.setOnClickListener(viewSix -> { chooseImage(5); });

        mImageViewProfileOne = findViewById(R.id.imageViewProfileOne);
        mImageViewProfileTwo = findViewById(R.id.imageViewProfileTwo);
        mImageViewProfileThree = findViewById(R.id.imageViewProfileThree);
        mImageViewProfileFour = findViewById(R.id.imageViewProfileFour);
        mImageViewProfileFive = findViewById(R.id.imageViewProfileFive);
        mImageViewProfileSix = findViewById(R.id.imageViewProfileSix);

        Button btnProceed = findViewById(R.id.buttonProceedPhotos);
        btnProceed.setOnClickListener(view -> {
            if (mPathOneSet) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase mUserDb = FirebaseDatabase.getInstance();
                DatabaseReference mUserRef = mUserDb.getReference();

                mUserRef.child("users").child("profile").child("account").child(mAuth.getCurrentUser().getUid())
                        .child("isFirstUser").setValue("false").addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                Intent mIntent = new Intent(this, HomeActivity.class);
                                startActivity(mIntent);
                                Bungee.slideLeft(this);
                            } else {
                                Toast.makeText(this, "Dear Customer, There was a network error kindly try again", Toast.LENGTH_SHORT).show();
                            }

                });
            } else {
                Toast.makeText(this, "Dear Customer, Kindly add at least first photo to proceed", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Uri mImageViewPathOne = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathOne);
                        mImageViewProfileOne.setImageBitmap(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] dataByte = baos.toByteArray();

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        UploadTask uploadTask = mStorageRef
                                .child("users").child("gallery").child(mUser.getUid())
                                .child("/main.jpg").putBytes(dataByte);
                        uploadTask.addOnCompleteListener(taskOne -> {
                            if (taskOne.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                mPathOneSet = true;
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
                case 1:
                    Uri mImageViewPathTwo = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathTwo);
                        mImageViewProfileTwo.setImageBitmap(bitmap);
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
                case 2:
                    Uri mImageViewPathThree = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathThree);
                        mImageViewProfileThree.setImageBitmap(bitmap);
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
                case 3:
                    Uri mImageViewPathFour = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathFour);
                        mImageViewProfileFour.setImageBitmap(bitmap);
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
                case 4:
                    Uri mImageViewPathFive = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathFive);
                        mImageViewProfileFive.setImageBitmap(bitmap);
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
                case 5:
                    Uri mImageViewPathSix = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageViewPathSix);
                        mImageViewProfileSix.setImageBitmap(bitmap);
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
