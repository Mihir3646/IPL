package com.ipl.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipl.R;
import com.ipl.model.JoinedLeagueModel;
import com.ipl.model.LeagueMemberListModel;

import java.util.ArrayList;


public class LeagueMemberListAdapter extends BaseAdapter {
    private ArrayList<LeagueMemberListModel> resultList;
    private Fragment fragment;
    private Context context;

    public LeagueMemberListAdapter(ArrayList<LeagueMemberListModel> resultList, Fragment fragment, Context context) {
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
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_data_league_member_list, viewGroup, false);
        }
        final TextView tvLeagueMemberName = (TextView) convertView.findViewById(R.id.row_item_league_member_tv_member_name);
        final LeagueMemberListModel leagueMemberListModel = (LeagueMemberListModel) getItem(i);
        tvLeagueMemberName.setText(leagueMemberListModel.getLeagueMemberName());
        return convertView;
    }
}
