package com.ipl.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ipl.R;
import com.ipl.activity.HomeActivity;
import com.ipl.activity.SignInActivity;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.LeagueDetailsModel;
import com.ipl.model.UserDetailsModel;
import com.ipl.utils.Utils;


public class CreateLeagueFragment extends Fragment implements View.OnClickListener {
    private EditText edtLeagueName;
    final String TAG = "CreateLeagueFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_league, container, false);
        initView(view);
        return view;
    }

    /**
     * method is for the initialization of component
     */
    private void initView(final View view) {
        final Button btnCreateLeague = (Button) view.findViewById(R.id.fragment_create_league_btn_create_league);
        edtLeagueName = (EditText) view.findViewById(R.id.create_league_fragment_edt_league_nam);
        btnCreateLeague.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_create_league_btn_create_league:
                if (TextUtils.isEmpty(edtLeagueName.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter your league name", Toast.LENGTH_SHORT).show();
                } else {
                    storeLeagueData();
                }
                break;
        }
    }

    /**
     * This method is to store the league name
     */
    private void storeLeagueData() {
        final Firebase ref = new Firebase(FirebaseConstant.FIREBASE_URL);
        final LeagueDetailsModel leagueDetailsModel = new LeagueDetailsModel();
        leagueDetailsModel.setLeagueName(edtLeagueName.getText().toString().trim());
        ref.child("league").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (dataSnapshot.child(edtLeagueName.getText().toString().trim()).exists()) {
                        //do ur stuff
                        Toast.makeText(getActivity(), "League name already in use", Toast.LENGTH_SHORT).show();

                    } else {
                        //do something
                        ref.child("league").child(edtLeagueName.getText().toString()).
                                setValue(FirebaseAuth.getInstance().getCurrentUser().getUid(), new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {
                                            Log.e(TAG, firebaseError.getMessage());
                                        } else {
                                            Utils.replaceFragment(getActivity(), new InviteFriendFragment(), R.id.activity_home_container);
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
//        ref.child("league").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("createdLeague")
//                .child(ref.child("league").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("createdLeague").push().getKey())
//                .setValue(edtLeagueName.getText().toString().trim(), new Firebase.CompletionListener() {
//                    @Override
//                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                        if (firebaseError != null) {
//                            Log.e(TAG, firebaseError.getMessage());
//                        } else {
//                            Utils.replaceFragment(getActivity(), new InviteFriendFragment(), R.id.activity_home_container);
//                        }
//                    }
//                });
    }

}
