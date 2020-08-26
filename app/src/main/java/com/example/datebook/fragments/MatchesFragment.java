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
import com.example.datebook.adapter.MatchRecyclerViewAdapter;
import com.example.datebook.model.MatchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesFragment extends Fragment {

    private List<MatchModel> modelList;
    private MatchRecyclerViewAdapter adapter;

    private FirebaseDatabase mMatchDb;
    private DatabaseReference mMatchRef;
    private ProgressBar progressBar;

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_matches, container, false);

        progressBar = mView.findViewById(R.id.progressMatches);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        RecyclerView mMatchList = mView.findViewById(R.id.match_list);
        mMatchList.setHasFixedSize(true);
        mMatchList.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        mMatchList.addItemDecoration(itemDecoration);

        modelList = new ArrayList<>();
        if (mAuth.getCurrentUser() != null) {
            // get the gender of our current user
            mMatchDb = FirebaseDatabase.getInstance();
            mMatchRef = mMatchDb.getReference().child("users").child("profile").child(mAuth.getCurrentUser().getUid());
            mMatchRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String gender = snapshot.child("gender").getValue().toString();

                    if (gender.equals("male")) {
                        // populate female matches
                        mMatchRef = mMatchDb.getReference().child("users").child("matches").child("female");
                        mMatchRef.keepSynced(true);
                        mMatchRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        MatchModel model = dataSnapshot.getValue(MatchModel.class);
                                        modelList.add(model);
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapter = new MatchRecyclerViewAdapter(modelList);
                                    mMatchList.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if (gender.equals("female")){
                        // populate male matches
                        mMatchRef = mMatchDb.getReference().child("users").child("matches").child("male");
                        mMatchRef.keepSynced(true);
                        mMatchRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                        MatchModel model = dataSnapshot.getValue(MatchModel.class);
                                        modelList.add(model);
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    adapter = new MatchRecyclerViewAdapter(modelList);
                                    mMatchList.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return mView;
    }
}
