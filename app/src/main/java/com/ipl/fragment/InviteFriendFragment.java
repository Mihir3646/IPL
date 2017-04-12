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


public class InviteFriendFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        initView(view);
        return view;
    }

    /**
     * method is for the initialization of component
     */
    private void initView(final View view) {
        final Button btnCreateLeague = (Button) view.findViewById(R.id.fragment_invite_friend_btn_invite_friend);
        btnCreateLeague.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_invite_friend_btn_invite_friend:
                Utils.replaceFragment(getActivity(), new MatchFragment(), R.id.activity_home_container);
                break;
        }
    }
}
