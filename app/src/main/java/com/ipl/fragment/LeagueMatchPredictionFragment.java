package com.ipl.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ipl.R;
import com.ipl.utils.Utils;


public class LeagueMatchPredictionFragment extends Fragment implements View.OnClickListener {
    private Button btnMatch;
    private Button btnLeaderBoard;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_league_match_prediction, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        btnMatch = (Button) view.findViewById(R.id.fragment_league_match_prediction_btn_match);
        btnLeaderBoard = (Button) view.findViewById(R.id.fragment_league_match_prediction_btn_leader_board);

        btnMatch.setOnClickListener(this);
        btnLeaderBoard.setOnClickListener(this);

        Utils.replaceFragment(getActivity(), new MatchFragment(), R.id.fragment_league_match_prediction_container);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_league_match_prediction_btn_match:
                btnMatch.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
                btnLeaderBoard.setTextColor(Color.WHITE);
                Utils.replaceFragment(getActivity(), new MatchFragment(), R.id.fragment_league_match_prediction_container);
                break;

            case R.id.fragment_league_match_prediction_btn_leader_board:
                btnMatch.setTextColor(Color.WHITE);
                btnLeaderBoard.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
                Utils.replaceFragment(getActivity(), new LeagueLeaderBoardFragment(), R.id.fragment_league_match_prediction_container);
                break;
        }

    }
}
