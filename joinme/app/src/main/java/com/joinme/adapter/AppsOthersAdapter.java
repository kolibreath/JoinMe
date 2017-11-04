package com.joinme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joinme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwxwl on 2017/10/21.
 */

public class AppsOthersAdapter extends RecyclerView.Adapter<AppsOthersViewHolder> {

    private List<String> list;
    private Context mContext;
    private List<String> blackList = new ArrayList<>();
    public AppsOthersAdapter(Context mContext, List<String> mAppInfos) {
        this.mContext = mContext;
        this.list = mAppInfos;
    }

    @Override
    public AppsOthersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_others, null);
        AppsOthersViewHolder appsViewHolder = new AppsOthersViewHolder(layoutView);
        return appsViewHolder;
    }


    @Override
    public void onBindViewHolder(AppsOthersViewHolder holder, final int position) {
        holder.mAppOthersName.setText(list.get(position));
        holder.mIsBanned.setChecked(false);
        holder.mIsBanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackList.add(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public List<String> getBlackList() {
        return blackList;
    }
}
