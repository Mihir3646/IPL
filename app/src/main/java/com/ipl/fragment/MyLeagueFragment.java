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


public class MyLeagueFragment extends Fragment implements View.OnClickListener {
    private Button btnCreatedLeague;
    private Button btnJoinedLeague;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_league, container, false);
        initView(view);
        return view;
    }

    /**
     * This method is for the initialization of component
     */
    private void initView(final View view) {
        btnCreatedLeague = (Button) view.findViewById(R.id.fragment_my_league_btn_created);
        btnJoinedLeague = (Button) view.findViewById(R.id.fragment_my_league_btn_joined);

        btnCreatedLeague.setOnClickListener(this);
        btnJoinedLeague.setOnClickListener(this);

        Utils.replaceFragment(getActivity(), new CreatedLeagueFragment(), R.id.fragment_my_league_container);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_my_league_btn_created:
                btnCreatedLeague.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
                btnJoinedLeague.setTextColor(Color.WHITE);
                Utils.replaceFragment(getActivity(), new CreatedLeagueFragment(), R.id.fragment_my_league_container);
                break;

            case R.id.fragment_my_league_btn_joined:
                btnCreatedLeague.setTextColor(Color.WHITE);
                btnJoinedLeague.setTextColor(ContextCompat.getColor(getActivity(), R.color.blueColor));
                Utils.replaceFragment(getActivity(), new JoinedLeagueFragment(), R.id.fragment_my_league_container);
                break;
        }
    }
}
