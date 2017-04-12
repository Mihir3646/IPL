package com.ipl.adapter;


import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipl.R;
import com.ipl.model.LeagueLeaderBoardModel;

import java.util.ArrayList;

public class LeagueLeaderBoardListAdapter extends BaseAdapter {
    private ArrayList<LeagueLeaderBoardModel> resultList;
    private Fragment fragment;
    private Context context; //context

    public LeagueLeaderBoardListAdapter(ArrayList<LeagueLeaderBoardModel> resultList, Fragment fragment, Context context) {
        this.resultList = resultList;
        this.fragment = fragment;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_data_league_leader_board_list, viewGroup, false);
        }
        final TextView tvMemberRank = (TextView) convertView.findViewById(R.id.row_data_league_leader_board_member_rank);
        final TextView tvMemberName = (TextView) convertView.findViewById(R.id.row_data_league_leader_board_member_name);
        final TextView tvMemberPoint = (TextView) convertView.findViewById(R.id.row_data_league_leader_board_member_point);
        final LeagueLeaderBoardModel leagueLeaderBoardModel = (LeagueLeaderBoardModel) getItem(i);
        tvMemberName.setText(leagueLeaderBoardModel.getMemberName());
        tvMemberRank.setText(leagueLeaderBoardModel.getMemberRank());
        tvMemberPoint.setText(leagueLeaderBoardModel.getMemberPoint());
        return convertView;
    }
}
