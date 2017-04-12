package com.ipl.adapter;


import android.app.Fragment;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipl.R;
import com.ipl.fragment.JoinLeagueFragment;
import com.ipl.model.JoinLeagueModel;

import java.util.ArrayList;

public class JoinLeagueAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context; //context
    private ArrayList<JoinLeagueModel> resultList; //data source of the list adapter
    private Fragment fragment;
    private TextView tvLeagueName;
    private Button btnJoinLeague;

    public JoinLeagueAdapter(Context context, ArrayList<JoinLeagueModel> resultList, final Fragment fragment) {
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_data_join_league, viewGroup, false);
        }
        final JoinLeagueModel joinLeagueModel = (JoinLeagueModel) getItem(i);
        tvLeagueName = (TextView) convertView.findViewById(R.id.row_item_join_league_tv_league_name);
        btnJoinLeague = (Button) convertView.findViewById(R.id.row_item_join_league_btn_join_league);
        btnJoinLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Button btnJoin = (Button) view.findViewById(R.id.row_item_join_league_btn_join_league);
                ((JoinLeagueFragment) fragment).requestJoinLeague(joinLeagueModel.getLeagueName());
                btnJoin.setText("pending");
                btnJoin.setBackgroundColor(ContextCompat.getColor(context, R.color.redColor));
                Toast.makeText(context, "clicked" + i, Toast.LENGTH_SHORT).show();
            }
        });
        tvLeagueName.setText(joinLeagueModel.getLeagueName());
        return convertView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.row_item_join_league_btn_join_league:

                break;
        }
    }
}
