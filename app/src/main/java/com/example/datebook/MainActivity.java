package com.example.datebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datebook.auth.CreateAccountFour;
import com.example.datebook.auth.CreateAccountOne;
import com.example.datebook.auth.CreateAccountTwo;
import com.example.datebook.auth.CreateAccountThree;
import com.example.datebook.model.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private FirebaseDatabase mUserAuthDb;
    private DatabaseReference mUserAuthRef;
    private static final int RC_SIGN_IN = 9001;

    // for test purpose only
    private String testNumber = "+254714251069";
    private String testVerificationCode = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        mAuth = FirebaseAuth.getInstance();
        mUserAuthDb = FirebaseDatabase.getInstance();
        mUserAuthRef = mUserAuthDb.getReference();

        mStartLayout = findViewById(R.id.layout_start);

        progressBar = findViewById(R.id.progress);

        SignInButton btnSignIn = findViewById(R.id.sign_in_button);
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);

        btnSignIn.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        Spinner spinner = findViewById(R.id.spinnerMain);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.language_array,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
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
                                                                String publicName = dataSnapshot.child("publicName").getValue().toString();
                                                                String gender = dataSnapshot.child("gender").getValue().toString();
                                                                String dob = dataSnapshot.child("dob").getValue().toString();
                                                                String gallery = dataSnapshot.child("gallery").getValue().toString();

                                                                // if public name is empty redirect to create account one fragment
                                                                if (publicName.isEmpty()) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Intent mIntent = new Intent(MainActivity.this, CreateAccountOne.class);
                                                                    startActivity(mIntent);
                                                                    Bungee.slideLeft(MainActivity.this);
                                                                } else {
                                                                    if (gender.isEmpty()) {
                                                                        // check for gender
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Intent mIntent = new Intent(MainActivity.this, CreateAccountTwo.class);
                                                                        startActivity(mIntent);
                                                                        Bungee.slideLeft(MainActivity.this);
                                                                    } else {
                                                                        if (dob.isEmpty()) {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Intent mIntent = new Intent(MainActivity.this, CreateAccountThree.class);
                                                                            startActivity(mIntent);
                                                                            Bungee.slideLeft(MainActivity.this);
                                                                        } else {
                                                                            if (gallery.isEmpty()) {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                Intent mIntent = new Intent(MainActivity.this, CreateAccountFour.class);
                                                                                startActivity(mIntent);
                                                                                Bungee.slideLeft(MainActivity.this);
                                                                            }
                                                                            else {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                Intent mIntent = new Intent(MainActivity.this, HomeActivity.class);
                                                                                startActivity(mIntent);
                                                                                Bungee.slideLeft(MainActivity.this);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(MainActivity.this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            } else {
                                                Intent mIntent = new Intent(MainActivity.this, HomeActivity.class);
                                                startActivity(mIntent);
                                                Bungee.slideLeft(MainActivity.this);
                                            }

                                        } else {
                                            // create Map for profile
                                            Date objDate = new Date();
                                            HashMap<String, String> mUserMap = new HashMap<>();
                                            mUserMap.put("phone", mUser.getPhoneNumber());
                                            mUserMap.put("isFirstUser", getString(R.string.value_true));
                                            mUserMap.put("createProfileDate", String.valueOf(objDate));

                                            mUserAuthRef.child("users").child("profile").child("account").child(mUser.getUid())
                                                    .setValue(mUserMap).addOnCompleteListener(MainActivity.this, task -> {

                                                if (task.isSuccessful()) {
                                                    // open Map for user profile
                                                    HashMap<String, String> mProfileMap = new HashMap<>();
                                                    mProfileMap.put("publicName", "");
                                                    mProfileMap.put("gallery", "");
                                                    mProfileMap.put("gender", "");
                                                    mProfileMap.put("dob", "");
                                                    mProfileMap.put("status", getString(R.string.status));
                                                    mProfileMap.put("publicThumbnail", mUser.getPhotoUrl().toString());
                                                    mProfileMap.put("firstName", mUser.getDisplayName());
                                                    mProfileMap.put("lastName", "");

                                                    mUserAuthRef.child("users").child("profile").child(mUser.getUid())
                                                            .setValue(mProfileMap).addOnCompleteListener(MainActivity.this, profileTask -> {

                                                        if (profileTask.isSuccessful()) {
                                                            // user is ready for user registration process
                                                            progressBar.setVisibility(View.GONE);
                                                            Intent mIntent = new Intent(MainActivity.this, CreateAccountOne.class);
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
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, R.string.credential_auth_error, Toast.LENGTH_SHORT).show();
                    }
                });
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
