package com.ipl.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.ipl.adapter.PendingRequestListAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.PendingRequestListModel;

import java.util.ArrayList;


public class PendingRequestListFragment extends Fragment {
    final String TAG = "PendingRequestList";
    private String key;
    private ListView lvPendingRequestList;
    private PendingRequestListAdapter pendingRequestListAdapter;
    private ArrayList<PendingRequestListModel> pendingRequestListModelArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pending_request_list, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        lvPendingRequestList = (ListView) view.findViewById(R.id.fragment_pending_request_lv_pending_request_list);
        pendingRequestListAdapter = new PendingRequestListAdapter(getActivity(), pendingRequestListModelArrayList, PendingRequestListFragment.this);
        lvPendingRequestList.setAdapter(pendingRequestListAdapter);
        getAllPendingRequest();
    }

    /**
     * This method is to get all the pending request
     */
    private void getAllPendingRequest() {
        final String leagueName = ((HomeActivity) getActivity()).getLeagueName();
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_leagueJoinRequest))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (postSnapshot.child(getString(R.string.db_key_leagueName)).getValue().equals(leagueName)) {
                                final String userId = postSnapshot.child(getString(R.string.db_key_requestFromId)).getValue().toString();
                                firebase.child(getString(R.string.db_key_user)).child(userId).child(getString(R.string.db_key_userNickName)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final PendingRequestListModel pendingRequestListModel = new PendingRequestListModel();
                                        pendingRequestListModel.setRequestFromName(dataSnapshot.getValue().toString());
                                        pendingRequestListModelArrayList.add(pendingRequestListModel);
                                        pendingRequestListAdapter.notifyDataSetChanged();
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

        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_leagueJoinRequest))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pendingRequestListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
    }

    /**
     * This method is to delete pending request
     */
    public void removeRequest(final String requestFromName) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "onDataChange:");
                    if (postSnapshot.child(getString(R.string.db_key_userNickName)).getValue().equals(requestFromName)) {
                        final String key = postSnapshot.getKey();
                        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                child(getString(R.string.db_key_leagueJoinRequest)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (postSnapshot.child(getString(R.string.db_key_requestFromId)).getValue().equals(key)) {
                                        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                child(getString(R.string.db_key_leagueJoinRequest)).child(postSnapshot.getKey()).removeValue();
                                    }
                                }
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
     * This method is for accept request in league
     */
    public void acceptRequest(final String requestFromName) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_user)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "onDataChange:");
                    if (postSnapshot.child(getString(R.string.db_key_userNickName)).getValue().equals(requestFromName)) {
                        key = postSnapshot.getKey();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child(getString(R.string.db_key_user)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_myLeague)).
                        child(((HomeActivity) getActivity()).getLeagueName()).child(firebase.child(getString(R.string.db_key_user))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_myLeague)).
                                child(((HomeActivity) getActivity()).getLeagueName()).push().getKey()).setValue(key, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {

                        } else {
                            final Firebase firebase1 = new Firebase(FirebaseConstant.FIREBASE_URL);
                            firebase1.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child(getString(R.string.db_key_leagueJoinRequest)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.child(getString(R.string.db_key_requestFromId)).getValue().equals(key)) {
                                            firebase1.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                    child(getString(R.string.db_key_leagueJoinRequest)).child(postSnapshot.getKey()).removeValue();
                                        }
                                    }
                                    updateRequestStatus(key);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * This method is for the request join league status update
     */
    private void updateRequestStatus(final String requestFromUserId) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        final String leagueName = ((HomeActivity) getActivity()).getLeagueName();
        firebase.child(getString(R.string.db_key_user)).child(key).child(getString(R.string.db_key_joinedLeague)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "onDataChange: ");
                    if (postSnapshot.child(getString(R.string.db_key_leagueName)).getValue().equals(leagueName)) {
                        firebase.child("user").child(key).child(getString(R.string.db_key_joinedLeague)).child(postSnapshot.getKey()).child(getString(R.string.db_key_status))
                                .setValue(getString(R.string.accepted), new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

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
