package com.joinme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joinme.R;
import com.joinme.model.AppInfos;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsViewHolder> {

    private List<AppInfos> mAppInfos;
    private Context mContext;
    private List<AppInfos> mBannedList = new ArrayList<>();

    public AppsAdapter(Context mContext, List<AppInfos> mAppInfos) {
        this.mContext = mContext;
        this.mAppInfos = mAppInfos;
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, null);
        AppsViewHolder appsViewHolder = new AppsViewHolder(layoutView);
        return appsViewHolder;
    }


    @Override
    public void onBindViewHolder(AppsViewHolder holder, final int position) {
        holder.mAppLogo.setImageDrawable(mAppInfos.get(position).getIcon());
        holder.mAppName.setText(mAppInfos.get(position).getLabel());
        holder.mAppLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBannedList.add(new AppInfos(mAppInfos.get(position).getIcon()
                ,mAppInfos.get(position).getLabel()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mAppInfos.size();
    }

    public List<AppInfos> getmBannedList() {
        return mBannedList;
    }
}

