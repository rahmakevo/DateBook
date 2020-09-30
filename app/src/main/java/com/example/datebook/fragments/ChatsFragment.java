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
import android.widget.TextView;

import com.example.datebook.R;
import com.example.datebook.adapter.ChatRecyclerViewAdapter;
import com.example.datebook.adapter.NewMatchesRecyclerAdapter;
import com.example.datebook.model.InitiateChatModel;
import com.example.datebook.model.MatchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatsFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView, recyclerViewMatches;
    private List<InitiateChatModel> modelList;
    private List<MatchModel> matchModel;
    private List<String> mFriendsIdList;
    private ChatRecyclerViewAdapter adapter;
    private NewMatchesRecyclerAdapter matchesAdapter;

    private FirebaseDatabase mChatDb, mMatchDb, mMatchPrefDb;
    private DatabaseReference mChatRef, mMatchRef, mMatchPrefRef;
    private FirebaseAuth mAuth;

    // Required empty public constructor
    public ChatsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mChatDb = FirebaseDatabase.getInstance();
        mChatRef = mChatDb.getReference();
        mMatchDb = FirebaseDatabase.getInstance();
        mMatchRef = mMatchDb.getReference();
        mMatchPrefDb = FirebaseDatabase.getInstance();
        mMatchPrefRef = mMatchPrefDb.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_chats, container, false);

        TextView mTextNoMatches = mView.findViewById(R.id.textMatchesAvailable);
        progressBar = mView.findViewById(R.id.progressChats);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = mView.findViewById(R.id.chats_list);
        recyclerViewMatches = mView.findViewById(R.id.matches_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        modelList = new ArrayList<>();
        mChatRef = mChatRef.child("users").child("chat").child(mAuth.getCurrentUser().getUid()).child("initiateChat");
        mChatRef.keepSynced(true);
        mChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    InitiateChatModel model = dataSnapshot.getValue(InitiateChatModel.class);
                    modelList.add(model);
                }

                progressBar.setVisibility(View.GONE);
                adapter = new ChatRecyclerViewAdapter(modelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewMatches.setHasFixedSize(true);
        recyclerViewMatches.setLayoutManager(layoutManager);

        matchModel = new ArrayList<>();
        mMatchRef = mMatchRef.child("users").child("profile").child(mAuth.getCurrentUser().getUid());
        mMatchRef.keepSynced(true);
        mMatchRef.orderByValue();
        mMatchRef.limitToFirst(50);
        mMatchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mGender = snapshot.child("gender").getValue().toString();

                if (mGender.equals("male")) {
                    FirebaseDatabase mInitiateChatCheckDb = FirebaseDatabase.getInstance();
                    DatabaseReference mInitiateChatCheckRef = mInitiateChatCheckDb.getReference();
                    mInitiateChatCheckRef
                            .child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                            .child("initiateChat").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mFriendsIdList = new ArrayList<>();
                            mFriendsIdList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                InitiateChatModel model = dataSnapshot.getValue(InitiateChatModel.class);
                                mFriendsIdList.add(model.recipient_id);
                            }

                            // create MatchPrefSettings
                            HashMap<String, String> mMatchPrefMap = new HashMap<>();
                            mMatchPrefMap.put("discovery", "true");
                            mMatchPrefMap.put("matchGenderType", "female");
                            mMatchPrefMap.put("agePref", "18");
                            mMatchPrefMap.put("countryFilter", "false");
                            mMatchPrefMap.put("localityFilter", "false");

                            mMatchPrefRef
                                    .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                    .child("settings").child("matchPreferences")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                mMatchPrefRef.child("users").child("profile")
                                                        .child(mAuth.getCurrentUser().getUid())
                                                        .child("settings").child("matchPreferences")
                                                        .setValue(mMatchPrefMap).addOnSuccessListener(task -> {});
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            // show female matches
                            matchModel.clear();
                            mMatchRef = mMatchDb.getReference().child("users").child("matches").child("female");
                            mMatchRef.keepSynced(true);
                            mMatchRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        matchModel.clear();
                                        for (int i = 0; i < mFriendsIdList.size(); i++) {
                                            if (!snapshot.hasChild(mFriendsIdList.get(i))) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    MatchModel model = dataSnapshot.getValue(MatchModel.class);
                                                    matchModel.add(model);
                                                }
                                            } else {
                                                mTextNoMatches.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        progressBar.setVisibility(View.GONE);
                                        matchesAdapter = new NewMatchesRecyclerAdapter(matchModel);
                                        recyclerViewMatches.setAdapter(matchesAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (mGender.equals("female")) {
                    FirebaseDatabase mInitiateChatCheckDb = FirebaseDatabase.getInstance();
                    DatabaseReference mInitiateChatCheckRef = mInitiateChatCheckDb.getReference();
                    mInitiateChatCheckRef
                            .child("users").child("chat").child(mAuth.getCurrentUser().getUid())
                            .child("initiateChat").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mFriendsIdList = new ArrayList<>();
                            mFriendsIdList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                InitiateChatModel model = dataSnapshot.getValue(InitiateChatModel.class);
                                mFriendsIdList.add(model.recipient_id);
                            }

                            // create MatchPrefSettings
                            HashMap<String, String> mMatchPrefMap = new HashMap<>();
                            mMatchPrefMap.put("discovery", "true");
                            mMatchPrefMap.put("matchGenderType", "male");
                            mMatchPrefMap.put("agePref", "18");
                            mMatchPrefMap.put("countryFilter", "false");
                            mMatchPrefMap.put("localityFilter", "false");

                            mMatchPrefRef
                                    .child("users").child("profile").child(mAuth.getCurrentUser().getUid())
                                    .child("settings").child("matchPreferences")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                mMatchPrefRef.child("users").child("profile")
                                                        .child(mAuth.getCurrentUser().getUid())
                                                        .child("settings").child("matchPreferences")
                                                        .setValue(mMatchPrefMap).addOnSuccessListener(task -> {});
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            // show male matches
                            matchModel.clear();
                            mMatchRef = mMatchDb.getReference().child("users").child("matches").child("male");
                            mMatchRef.keepSynced(true);
                            mMatchRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        matchModel.clear();
                                        for (int i = 0; i < mFriendsIdList.size(); i++) {
                                            if (!snapshot.hasChild(mFriendsIdList.get(i))) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    MatchModel model = dataSnapshot.getValue(MatchModel.class);
                                                    matchModel.add(model);
                                                }
                                            } else {
                                                mTextNoMatches.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        progressBar.setVisibility(View.GONE);
                                        matchesAdapter = new NewMatchesRecyclerAdapter(matchModel);
                                        recyclerViewMatches.setAdapter(matchesAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
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


        return mView;
    }
}
