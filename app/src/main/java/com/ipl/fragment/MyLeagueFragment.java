package com.ipl.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ipl.R;
import com.ipl.utils.Utils;


public class MyLeagueFragment extends Fragment implements View.OnClickListener {

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
        final Button btnCreatedLeague = (Button) view.findViewById(R.id.fragment_my_league_btn_created);
        final Button btnJoinedLeague = (Button) view.findViewById(R.id.fragment_my_league_btn_joined);

        btnCreatedLeague.setOnClickListener(this);
        btnJoinedLeague.setOnClickListener(this);

        Utils.replaceFragment(getActivity(), new CreatedLeagueFragment(), R.id.fragment_my_league_container);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_my_league_btn_created:
                Utils.replaceFragment(getActivity(), new CreatedLeagueFragment(), R.id.fragment_my_league_container);
                break;

            case R.id.fragment_my_league_btn_joined:
                Utils.replaceFragment(getActivity(), new JoinedLeagueFragment(), R.id.fragment_my_league_container);
                break;
        }
    }
}
