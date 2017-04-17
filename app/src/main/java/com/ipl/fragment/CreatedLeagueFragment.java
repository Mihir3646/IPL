package com.ipl.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ipl.R;
import com.ipl.activity.HomeActivity;
import com.ipl.adapter.CreatedLeagueAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.CreatedLeagueModel;

import java.util.ArrayList;


public class CreatedLeagueFragment extends Fragment {
    private ListView lvCreatedLeagueList;
    private CreatedLeagueAdapter createdLeagueAdapter;
    private ArrayList<CreatedLeagueModel> createdLeagueModelArrayList = new ArrayList<>();
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_created_league, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        lvCreatedLeagueList = (ListView) view.findViewById(R.id.fragment_created_league_lv_list);
        createdLeagueAdapter = new CreatedLeagueAdapter(getActivity(), createdLeagueModelArrayList, CreatedLeagueFragment.this);
        lvCreatedLeagueList.setAdapter(createdLeagueAdapter);
        getCreatedLeagueList();
    }

    /**
     * This method is to get the created league list from db
     */
    private void getCreatedLeagueList() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_leagueName)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        final CreatedLeagueModel createdLeagueModel = new CreatedLeagueModel();
                        createdLeagueModel.setLeagueName(postSnapshot.getKey());
                        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_leagueJoinRequest))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            if (postSnapshot.child(getString(R.string.db_key_leagueName)).getValue().equals(createdLeagueModel.getLeagueName())) {
                                                count = count + 1;
                                            }
                                        }
                                        createdLeagueModel.setLeagueJoinRequestCount(count);
                                        createdLeagueModelArrayList.add(createdLeagueModel);
                                        count = 0;
                                        createdLeagueAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

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

    /**
     * method is to pass league name from one this fragment to pending request fragment
     */
    public void setLeagueName(final String leagueName) {
        ((HomeActivity) getActivity()).setLeagueName(leagueName);
    }

}
