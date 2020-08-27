package com.example.datebook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.datebook.R;
import com.example.datebook.adapter.CallsRecyclerAdapter;
import com.example.datebook.model.InitiateChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {

    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private List<InitiateChatModel> modelList;
    private CallsRecyclerAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mVideoDb;
    private DatabaseReference mVideoRef;

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mVideoDb = FirebaseDatabase.getInstance();
        mVideoRef = mVideoDb.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calls, container, false);

        mProgressBar = mView.findViewById(R.id.progressCalls);
        mProgressBar.setVisibility(View.VISIBLE);

        recyclerView = mView.findViewById(R.id.calls_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        modelList = new ArrayList<>();
        mVideoRef = mVideoRef.child("users").child("calls").child(mAuth.getCurrentUser().getUid()).child("initiateCall");
        mVideoRef.keepSynced(true);
        mVideoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    InitiateChatModel model = dataSnapshot.getValue(InitiateChatModel.class);
                    modelList.add(model);
                }

                mProgressBar.setVisibility(View.GONE);
                adapter = new CallsRecyclerAdapter(modelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mView;
    }
}
