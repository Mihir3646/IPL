package com.ipl.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ipl.R;
import com.ipl.activity.HomeActivity;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.MatchDetailsModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MatchFragment extends Fragment implements View.OnClickListener {
    private Button btnTeamOne;
    private Button btnTeamTwo;
    private TextView tvVs;
    private TextView tvMatchNumber;
    final String TAG = "MatchFragment";
    private String selectedTeam = null;
    private ArrayList<MatchDetailsModel> matchDetailsModelArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_match_one, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        tvVs = (TextView) view.findViewById(R.id.fragment_home_tv_vs);
        btnTeamTwo = (Button) view.findViewById(R.id.activity_home_btn_team_two);
        btnTeamOne = (Button) view.findViewById(R.id.activity_home_btn_team_one);
        tvMatchNumber = (TextView) view.findViewById(R.id.activity_home_tv_match_number);
        final Button btnSubmit = (Button) view.findViewById(R.id.activity_home_btn_submit);

        btnSubmit.setOnClickListener(this);
        btnTeamOne.setOnClickListener(this);
        btnTeamTwo.setOnClickListener(this);

        getTodayMatch();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_home_btn_team_one:
                selectedTeam = btnTeamOne.getText().toString();
                break;

            case R.id.activity_home_btn_team_two:
                selectedTeam = btnTeamTwo.getText().toString();
                break;

            case R.id.activity_home_btn_submit:
                if (selectedTeam != null) {
                    storePredictedResult(selectedTeam);
                } else {
                    Toast.makeText(getActivity(), "Please select one team", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * This method is to store predicted result into db
     */
    private void storePredictedResult(final String selectedTeam) {
        final String matchId = tvMatchNumber.getText().toString().trim();
        final String leagueName = ((HomeActivity) getActivity()).getLeagueName();
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_prediction)).child(leagueName).child(matchId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(selectedTeam, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(TAG, "failure");
                } else {
                    Log.e(TAG, "success");
                }
            }
        });
    }

    /**
     * This method is to get current match details
     */
    private void getTodayMatch() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);

        final DateFormat df = new SimpleDateFormat("HH"); //format time
        final int currentTime = Integer.parseInt(df.format(Calendar.getInstance().getTime()));

        final String todayDate = new SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().getTime());
        firebase.child(getString(R.string.db_key_match)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.child(getString(R.string.db_key_date)).getValue().equals(todayDate) && Integer.parseInt(postSnapshot.child(getString(R.string.db_key_time)).getValue().toString()) > currentTime) {
                        final MatchDetailsModel matchDetailsModel = new MatchDetailsModel();
                        matchDetailsModel.setTeamA(postSnapshot.child(getString(R.string.db_key_teamA)).getValue().toString());
                        matchDetailsModel.setTeamB(postSnapshot.child(getString(R.string.db_key_teamB)).getValue().toString());
                        matchDetailsModel.setMatchId(postSnapshot.getKey());
                        matchDetailsModel.setMatchTime(postSnapshot.child(getString(R.string.db_key_time)).getValue().toString());
                        matchDetailsModelArrayList.add(matchDetailsModel);

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.child(getString(R.string.db_key_match)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (matchDetailsModelArrayList.size() > 1) {
                    for (int i = 0; i < matchDetailsModelArrayList.size(); i++) {
                        if (matchDetailsModelArrayList.get(i).getMatchTime().equals("04")) {
                            btnTeamOne.setText(matchDetailsModelArrayList.get(i).getTeamA());
                            btnTeamTwo.setText(matchDetailsModelArrayList.get(i).getTeamB());
                            tvMatchNumber.setText(matchDetailsModelArrayList.get(i).getMatchId());
                        }
                    }
                } else {
                    for (int i = 0; i < matchDetailsModelArrayList.size(); i++) {
                        btnTeamOne.setText(matchDetailsModelArrayList.get(i).getTeamA());
                        btnTeamTwo.setText(matchDetailsModelArrayList.get(i).getTeamB());
                        tvMatchNumber.setText(matchDetailsModelArrayList.get(i).getMatchId());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
