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
import com.ipl.adapter.JoinedLeagueAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.JoinedLeagueModel;

import java.util.ArrayList;


public class JoinedLeagueFragment extends Fragment {
    final String TAG = "JoinLeagueFragment";
    private ListView lvJoinedLeagueList;
    private JoinedLeagueAdapter joinedLeagueAdapter;
    private ArrayList<JoinedLeagueModel> joinedLeagueModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_joined_league, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        lvJoinedLeagueList = (ListView) view.findViewById(R.id.fragment_joined_league_lv_list);
        joinedLeagueAdapter = new JoinedLeagueAdapter(getActivity(), joinedLeagueModelArrayList, JoinedLeagueFragment.this);
        lvJoinedLeagueList.setAdapter(joinedLeagueAdapter);
        getJoinedLeagueList();
    }

    /**
     * This method is to get the created league list from db
     */
    private void getJoinedLeagueList() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_joinedLeague))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final JoinedLeagueModel joinedLeagueModel = new JoinedLeagueModel();
                            joinedLeagueModel.setLeagueName(postSnapshot.child(getString(R.string.db_key_leagueName)).getValue().toString());
                            joinedLeagueModel.setStatus(postSnapshot.child(getString(R.string.db_key_status)).getValue().toString());
                            joinedLeagueModelArrayList.add(joinedLeagueModel);

                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.db_key_joinedLeague))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        joinedLeagueAdapter.notifyDataSetChanged();
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
