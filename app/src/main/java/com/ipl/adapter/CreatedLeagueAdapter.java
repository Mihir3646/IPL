package com.ipl.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipl.R;
import com.ipl.fragment.CreatedLeagueFragment;
import com.ipl.fragment.LeagueMemberListFragment;
import com.ipl.fragment.PendingRequestListFragment;
import com.ipl.model.CreatedLeagueModel;
import com.ipl.utils.Utils;

import java.util.ArrayList;


public class CreatedLeagueAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<CreatedLeagueModel> resultList; //data source of the list adapter
    private Fragment fragment;

    public CreatedLeagueAdapter(Context context, ArrayList<CreatedLeagueModel> resultList, final Fragment fragment) {
        this.context = context;
        this.resultList = resultList;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_created_league, viewGroup, false);
        }
        final CreatedLeagueModel createdLeagueModel = (CreatedLeagueModel) getItem(i);
        final LinearLayout llCreatedLeague = (LinearLayout) convertView.findViewById(R.id.row_item_created_league);
        final TextView tvLeagueName = (TextView) convertView.findViewById(R.id.row_item_created_league_tv_league_name);
        tvLeagueName.setText(createdLeagueModel.getLeagueName());
        final TextView tvPendingRequest = (TextView) convertView.findViewById(R.id.row_item_created_league_tv_pending_request);
        if (createdLeagueModel.getLeagueJoinRequestCount() == 0) {
            tvPendingRequest.setVisibility(View.INVISIBLE);
        } else {
            tvPendingRequest.setText("" + createdLeagueModel.getLeagueJoinRequestCount());
        }
        tvPendingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.addFragment(fragment.getActivity(), new PendingRequestListFragment(), fragment, R.id.activity_home_container);
                ((CreatedLeagueFragment) fragment).setLeagueName(createdLeagueModel.getLeagueName());
            }
        });

        llCreatedLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.addFragment(fragment.getActivity(), new LeagueMemberListFragment(), fragment, R.id.activity_home_container);
                ((CreatedLeagueFragment) fragment).setLeagueName(createdLeagueModel.getLeagueName());
            }
        });

        return convertView;
    }

}
