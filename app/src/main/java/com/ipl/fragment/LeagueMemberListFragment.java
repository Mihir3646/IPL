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
import com.ipl.adapter.LeagueMemberListAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.LeagueMemberListModel;

import java.util.ArrayList;


public class LeagueMemberListFragment extends Fragment {
    private ArrayList<LeagueMemberListModel> leagueMemberListModelArrayList = new ArrayList<>();
    private ListView lvLeagueMemberList;
    private LeagueMemberListAdapter leagueMemberListAdapter;
    private String memberName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_league_member, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        lvLeagueMemberList = (ListView) view.findViewById(R.id.fragment_league_member_lv_league_member_list);
        leagueMemberListAdapter = new LeagueMemberListAdapter(leagueMemberListModelArrayList, LeagueMemberListFragment.this, getActivity());
        lvLeagueMemberList.setAdapter(leagueMemberListAdapter);
        getMemberList();
    }

    /**
     * This method is to get all the member list of league
     */
    private void getMemberList() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        final String leagueName = ((HomeActivity) getActivity()).getLeagueName();
        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.db_key_myLeague)).child(leagueName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    getMemberName(postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.db_key_myLeague)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                leagueMemberListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * This method is to get member name form member id
     */
    private void getMemberName(final String memberId) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).child(memberId).child(getString(R.string.db_key_userNickName))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final LeagueMemberListModel leagueMemberListModel = new LeagueMemberListModel();
                        memberName = dataSnapshot.getValue().toString();
                        leagueMemberListModel.setLeagueMemberName(memberName);
                        leagueMemberListModelArrayList.add(leagueMemberListModel);
                        leagueMemberListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

}
