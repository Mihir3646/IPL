package com.ipl.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ipl.R;
import com.ipl.adapter.JoinLeagueAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.JoinLeagueModel;
import com.ipl.model.RequestJoinLeagueModel;

import java.util.ArrayList;

public class JoinLeagueFragment extends Fragment {
    final String TAG = "JoinLeagueFragment";
    private ListView lvJoinLeagueList;
    private JoinLeagueAdapter joinLeagueAdapter;
    private ArrayList<JoinLeagueModel> joinLeagueModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_join_league, container, false);
        initView(view);
        return view;
    }

    /**
     * method is for the initialization of component
     */
    private void initView(final View view) {
        lvJoinLeagueList = (ListView) view.findViewById(R.id.fragment_join_league_lv_league_list);
        joinLeagueAdapter = new JoinLeagueAdapter(getActivity(), joinLeagueModelArrayList, JoinLeagueFragment.this);
        lvJoinLeagueList.setAdapter(joinLeagueAdapter);
        getOtherLeagueList();
    }

    /**
     * This method is to get the created league list from db
     */
    private void getOtherLeagueList() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child("league").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        final JoinLeagueModel joinLeagueModel = new JoinLeagueModel();
                        joinLeagueModel.setLeagueName(postSnapshot.getKey());
                        joinLeagueModelArrayList.add(joinLeagueModel);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child("league").

                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        joinLeagueAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    /**
     * method is to request join league
     */
    public void requestJoinLeague(final String leagueName) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        final JoinLeagueModel joinLeagueModel = new JoinLeagueModel();
        joinLeagueModel.setLeagueName(leagueName);
        joinLeagueModel.setStatus("pending");
        firebase.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("joinedLeague")
                .child(firebase.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("joinedLeague").push().getKey())
                .setValue(joinLeagueModel, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e(TAG, firebaseError.getMessage());
                        } else {
                            Log.e(TAG, "success");
                            joinLeagueRequest(leagueName);
                        }
                    }
                });

    }

    /**
     * This method is to make entry on league owner join league request node
     */
    private void joinLeagueRequest(final String leagueName) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child("league").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().matches(leagueName)) {
                        final String leagueOwnerId = postSnapshot.getValue().toString();
                        final RequestJoinLeagueModel requestJoinLeagueModel = new RequestJoinLeagueModel();
                        requestJoinLeagueModel.setLeagueName(leagueName);
                        requestJoinLeagueModel.setRequestFromId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        firebase.child("user").child(leagueOwnerId).child("leagueJoinRequest")
                                .child(firebase.child("user").child(leagueOwnerId).child("leagueJoinRequest").push().getKey())
                                .setValue(requestJoinLeagueModel, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {

                                        } else {

                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
