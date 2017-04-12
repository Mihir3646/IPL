package com.ipl.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipl.R;
import com.ipl.activity.HomeActivity;
import com.ipl.fragment.PendingRequestListFragment;
import com.ipl.model.PendingRequestListModel;

import java.util.ArrayList;


public class PendingRequestListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<PendingRequestListModel> resultList; //data source of the list adapter
    private Fragment fragment;

    public PendingRequestListAdapter(Context context, ArrayList<PendingRequestListModel> resultList, Fragment fragment) {
        this.context = context;
        this.resultList = resultList;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
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
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_pending_request_list, viewGroup, false);
        }
        final ImageView ivAccept = (ImageView) convertView.findViewById(R.id.row_item_pending_request_btn_accept);
        final ImageView ivReject = (ImageView) convertView.findViewById(R.id.row_item_join_pending_request_btn_reject);

        final TextView tvRequestFromName = (TextView) convertView.findViewById(R.id.row_item_pending_request_tv_request_from_name);
        final PendingRequestListModel pendingRequestListModel = (PendingRequestListModel) getItem(i);
        tvRequestFromName.setText(pendingRequestListModel.getRequestFromName());

        ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultList.remove(i);
                ((PendingRequestListFragment) fragment).removeRequest(pendingRequestListModel.getRequestFromName());
                notifyDataSetChanged();
            }
        });

        ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PendingRequestListFragment) fragment).acceptRequest(pendingRequestListModel.getRequestFromName());
                resultList.remove(i);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
