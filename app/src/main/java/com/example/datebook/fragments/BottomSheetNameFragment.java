package com.example.datebook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.datebook.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomSheetNameFragment extends BottomSheetDialogFragment {
    private String title;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDbProfile;
    private DatabaseReference mProfileRef;

    public BottomSheetNameFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDbProfile = FirebaseDatabase.getInstance();
        mProfileRef = mDbProfile.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.bottom_sheet_persistent, container, false);

        TextView mTextName = view.findViewById(R.id.textBottomSheetName);
        mTextName.setText(title);

        EditText mTextEdit = view.findViewById(R.id.editTextBottomSheet);
        switch (title) {
            case "Change your Public Name":
                mTextEdit.setFocusable(true);
                mTextEdit.setMaxLines(1);
                break;
            case "Change your Bio":
                mTextEdit.setFocusable(true);
                mTextEdit.setMaxLines(10);
                break;
        }

        TextView mTextCancel = view.findViewById(R.id.textViewBottomSheetCancelName);
        mTextCancel.setOnClickListener(viewCancel -> {
            dismiss();
        });

        TextView mTextSave = view.findViewById(R.id.textViewBottomSheetSaveName);
        mTextSave.setOnClickListener(viewSave -> {
            switch (title) {
                case "Change your Public Name":
                    String publicName = mTextEdit.getText().toString();

                    if (publicName.isEmpty()) {
                        mTextEdit.setError("Public Name cannot be empty");
                    } else {
                        mProfileRef.child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                .child("publicName").setValue(publicName).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dismiss();
                                    } else {
                                        mTextEdit.setError("Network Error. Kindly try saving Again!");
                                    }
                        });
                    }
                    break;
                case "Change your Bio":
                    String status = mTextEdit.getText().toString();

                    if (status.isEmpty()) {
                        mTextEdit.setError("Bio cannot be empty!");
                    } else {
                        mProfileRef.child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                .child("status").setValue(status).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                dismiss();
                            } else {
                                mTextEdit.setError("Network Error. Kindly try saving Again!");
                            }
                        });
                    }
                    break;
            }
        });
        return view;
    }
}
