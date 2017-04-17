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
import com.ipl.R;
import com.ipl.activity.HomeActivity;
import com.ipl.adapter.LeagueLeaderBoardListAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.LeagueLeaderBoardModel;

import java.util.ArrayList;


public class LeagueLeaderBoardFragment extends Fragment {
    private ArrayList<LeagueLeaderBoardModel> leagueLeaderBoardModelArrayList = new ArrayList<>();
    private ListView lvLeagueLeaderList;
    private LeagueLeaderBoardListAdapter leagueLeaderBoardListAdapter;
    private String memberName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_league_leader_board, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        lvLeagueLeaderList = (ListView) view.findViewById(R.id.fragment_league_leader_board_lv_leader_list);
        leagueLeaderBoardListAdapter = new LeagueLeaderBoardListAdapter(leagueLeaderBoardModelArrayList, LeagueLeaderBoardFragment.this, getActivity());
        lvLeagueLeaderList.setAdapter(leagueLeaderBoardListAdapter);
        getLeagueLeaderList();
    }

    /**
     * This method is for fetching the list from db
     */
    private void getLeagueLeaderList() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        final String leagueName = ((HomeActivity) getActivity()).getLeagueName();
        firebase.child(getString(R.string.db_key_leaderBoard)).child(leagueName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    firebase.child(getString(R.string.db_key_leaderBoard)).child(leagueName).child(postSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                                getUserDetails(postSnapshot1.getKey());
                                final LeagueLeaderBoardModel leagueLeaderBoardModel = new LeagueLeaderBoardModel();
                                leagueLeaderBoardModel.setMemberName(getMemberName());
                                leagueLeaderBoardModelArrayList.add(leagueLeaderBoardModel);
                            }
                            leagueLeaderBoardListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child(getString(R.string.db_key_leaderBoard)).child(leagueName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * This method is to get user details by id
     */
    private void getUserDetails(final String userID) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setMemberName(dataSnapshot.child(getString(R.string.db_key_userNickName)).getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * getter setter to get and set member name
     *
     * @return
     */
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
