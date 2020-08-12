package com.example.datebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datebook.auth.RegistrationFlowActivity;
import com.example.datebook.fragments.CreateAccountOne;
import com.example.datebook.model.MainViewModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout mStartLayout;
    EditText mEditTextNumber;
    CountryCodePicker cpp;
    protected MainViewModel mainViewModel;
    private ProgressBar progressBar;

    // user permissions variables
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA };

    // firebase
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String finalNumber = "";
    private FirebaseDatabase mUserAuthDb;
    private DatabaseReference mUserAuthRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        mUserAuthDb = FirebaseDatabase.getInstance();
        mUserAuthRef = mUserAuthDb.getReference();

        mStartLayout = findViewById(R.id.layout_start);
        mEditTextNumber = findViewById(R.id.editText_carrierNumber);
        progressBar = findViewById(R.id.progress);

        cpp = findViewById(R.id.ccp);
        cpp.registerCarrierNumberEditText(mEditTextNumber);

        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(v -> {
            Sprite chasingDots = new ChasingDots();
            progressBar.setVisibility(View.VISIBLE);

            String number = mEditTextNumber.getText().toString();

            if (number.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                mEditTextNumber.setError(getString(R.string.phone_empty_error));
            } else {
                PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber number1 = numberUtil.parse(cpp.getFullNumber(), cpp.getSelectedCountryNameCode());
                    boolean isValid = numberUtil.isValidNumber(number1);

                    if (isValid) {
                        finalNumber = cpp.getFullNumberWithPlus();
                        initiateSendVerificationCode(finalNumber);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        mEditTextNumber.setError(getString(R.string.phone_not_valid_error));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    mEditTextNumber.setError(e.getMessage());
                }
            }
        });

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // check for user if has account
                        FirebaseUser mUser = task.getResult().getUser();

                        mUserAuthRef.child("users").child("profile").child("account").child(mUser.getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            // check for firstUser else User has completed registration
                                            String firstUser = dataSnapshot.child("isFirstUser").getValue().toString();

                                            if (firstUser.equals(getString(R.string.value_true))) {
                                                // check for user profile Map for Profile Name Status
                                                mUserAuthRef.child("users").child("profile").child(mUser.getUid())
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                String publicName = dataSnapshot.getValue().toString();

                                                                // if public name is empty redirect to create account one fragment
                                                                if (publicName.isEmpty()) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Intent mIntent = new Intent(MainActivity.this, RegistrationFlowActivity.class);
                                                                    startActivity(mIntent);
                                                                    Bungee.slideLeft(MainActivity.this);
                                                                }

                                                                // check for gender

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(MainActivity.this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }

                                        } else {
                                            // create Map for profile
                                            Date objDate = new Date();
                                            HashMap<String, String> mUserMap = new HashMap<>();
                                            mUserMap.put("phone", finalNumber);
                                            mUserMap.put("isFirstUser", getString(R.string.value_true));
                                            mUserMap.put("createProfileDate", String.valueOf(objDate));

                                            mUserAuthRef.child("users").child("profile").child("account").child(mUser.getUid())
                                                    .setValue(mUserMap).addOnCompleteListener(MainActivity.this, task -> {

                                                        if (task.isSuccessful()) {
                                                            // open Map for user profile
                                                            HashMap<String, String> mProfileMap = new HashMap<>();
                                                            mProfileMap.put("publicName", "");
                                                            mProfileMap.put("gender", "");
                                                            mProfileMap.put("dob", "");
                                                            mProfileMap.put("status", getString(R.string.status));
                                                            mProfileMap.put("publicThumbnail", "");
                                                            mProfileMap.put("firstName", "");
                                                            mProfileMap.put("lastName", "");

                                                            mUserAuthRef.child("users").child("profile").child(mUser.getUid())
                                                                    .setValue(mProfileMap).addOnCompleteListener(MainActivity.this, profileTask -> {

                                                                        if (profileTask.isSuccessful()) {
                                                                            // user is ready for user registration process
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Intent mIntent = new Intent(MainActivity.this, RegistrationFlowActivity.class);
                                                                            startActivity(mIntent);
                                                                            Bungee.slideLeft(MainActivity.this);
                                                                        } else {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Toast.makeText(MainActivity.this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                            });
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(MainActivity.this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                                                        }

                                                    });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initiateSendVerificationCode(String finalNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                finalNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSelfPermissions();
        checkConnectivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkSelfPermissions();
        checkConnectivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSelfPermissions();
        checkConnectivity();
    }

    private void checkSelfPermissions() {
        final List<String> mMissingPermission = new ArrayList<>();

        for (final String mPermission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(MainActivity.this, mPermission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissingPermission.add(mPermission);
            }
        }

        if (!mMissingPermission.isEmpty()) {
            final String[] mPermission = mMissingPermission.toArray(new String[mMissingPermission.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, mPermission, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, R.string.request_permission_error, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }

    private void checkConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mInfo = manager.getActiveNetworkInfo();
        Boolean isConnected = mInfo != null && mInfo.isAvailable() && mInfo.isConnected();
        mainViewModel.setIsConnected(isConnected);
    }
}
