package com.ipl.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipl.R;
import com.ipl.fragment.JoinedLeagueFragment;
import com.ipl.fragment.LeagueMatchPredictionFragment;
import com.ipl.model.JoinedLeagueModel;
import com.ipl.utils.Utils;

import java.util.ArrayList;


public class JoinedLeagueAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<JoinedLeagueModel> resultList; //data source of the list adapter
    private Fragment fragment;

    public JoinedLeagueAdapter(Context context, ArrayList<JoinedLeagueModel> resultList, Fragment fragment) {
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
                    inflate(R.layout.row_data_joined_league, viewGroup, false);
        }
        final TextView tvLeagueName = (TextView) convertView.findViewById(R.id.row_item_joined_league_tv_league_name);
        final TextView tvStatus = (TextView) convertView.findViewById(R.id.row_item_joined_league_tv_status);
        final JoinedLeagueModel joinedLeagueModel = (JoinedLeagueModel) getItem(i);
        final LinearLayout llJoinedLeague = (LinearLayout) convertView.findViewById(R.id.row_data_joined_league);

        tvLeagueName.setText(joinedLeagueModel.getLeagueName());
        tvStatus.setText(joinedLeagueModel.getStatus());
        if (joinedLeagueModel.getStatus().equals(context.getString(R.string.db_key_pending))) {
            tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.redColor));
        } else if (joinedLeagueModel.getStatus().equals(context.getString(R.string.db_key_accepted))) {
//            tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGreen));
            tvStatus.setVisibility(View.INVISIBLE);
            llJoinedLeague.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.addFragment(fragment.getActivity(), new LeagueMatchPredictionFragment(), fragment, R.id.activity_home_container);
                    ((JoinedLeagueFragment) fragment).setLeagueName(joinedLeagueModel.getLeagueName());
                }
            });
        }
        return convertView;
    }
}
