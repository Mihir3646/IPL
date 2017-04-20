package com.ipl.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ipl.R;
import com.ipl.adapter.JoinLeagueAdapter;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.JoinLeagueModel;
import com.ipl.model.RequestJoinLeagueModel;
import com.ipl.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class JoinLeagueFragment extends Fragment implements TextWatcher {
    final String TAG = "JoinLeagueFragment";
    private ListView lvJoinLeagueList;
    private JoinLeagueAdapter joinLeagueAdapter;
    private EditText edtSearch;
    private ArrayList<JoinLeagueModel> joinLeagueModelArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;

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
        edtSearch = (EditText) view.findViewById(R.id.fragment_join_league_edt_search);
        lvJoinLeagueList = (ListView) view.findViewById(R.id.fragment_join_league_lv_league_list);
        joinLeagueAdapter = new JoinLeagueAdapter(getActivity(), joinLeagueModelArrayList, JoinLeagueFragment.this);
        lvJoinLeagueList.setAdapter(joinLeagueAdapter);
        edtSearch.addTextChangedListener(this);
        getOtherLeagueList();
    }

    /**
     * This method is to get the created league list from db
     */
    private void getOtherLeagueList() {
        progressDialog = Utils.showProgressDialog(getActivity(), getString(R.string.please_wait), false);
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_league)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        alreadyJoinedLeagueList(postSnapshot.getKey());
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

        firebase.child(getString(R.string.db_key_league)).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Utils.dismissProgressDialog(progressDialog);
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
        joinLeagueModel.setStatus(getString(R.string.db_key_pending));
        firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_joinedLeague))
                .child(firebase.child(getString(R.string.db_key_user)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.db_key_joinedLeague))
                        .push().getKey())
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
        firebase.child(getString(R.string.db_key_league)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().matches(leagueName)) {
                        final String leagueOwnerId = postSnapshot.getValue().toString();
                        final RequestJoinLeagueModel requestJoinLeagueModel = new RequestJoinLeagueModel();
                        requestJoinLeagueModel.setLeagueName(leagueName);
                        requestJoinLeagueModel.setRequestFromId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        firebase.child(getString(R.string.db_key_user)).child(leagueOwnerId).child(getString(R.string.db_key_leagueJoinRequest))
                                .child(firebase.child(getString(R.string.db_key_user)).child(leagueOwnerId).child(getString(R.string.db_key_leagueJoinRequest)).push().getKey())
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

    /**
     * this method is to get already joined league list
     */
    private void alreadyJoinedLeagueList(final String leagueName) {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        final Query query = firebase.child(getString(R.string.db_key_user))
                .orderByChild(getString(R.string.db_key_leagueName)).equalTo(leagueName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists()) {

                    }
                }
                joinLeagueAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                joinLeagueAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
        joinLeagueAdapter.filter(text);
    }
}
