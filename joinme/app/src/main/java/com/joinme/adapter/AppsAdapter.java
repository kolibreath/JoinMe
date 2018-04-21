package com.joinme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.joinme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolibreath on 2018/4/21.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

//    private Context context;
    private List<String> list;
    private List<String> blackList = new ArrayList<>();
    public AppsAdapter(List<String> mAppInfos) {
        this.list = mAppInfos;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_others, null);
        AppViewHolder appsViewHolder = new AppViewHolder(layoutView);
        return appsViewHolder;
    }


    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        holder.mAppOthersName.setText(list.get(position));
        holder.mIsBanned.setChecked(false);
        holder.mIsBanned.setOnClickListener(view -> blackList.add(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public List<String> getBlackList() {
        return blackList;
    }

    static class AppViewHolder extends RecyclerView.ViewHolder{
        public TextView mAppOthersName;
        public CheckBox mIsBanned;

        public AppViewHolder(View itemView) {
            super(itemView);
            //todo
//            itemView.setOnClickListener(this);

            mAppOthersName = (TextView) itemView.findViewById(R.id.txv_app_others_name);
            mIsBanned = (CheckBox) itemView.findViewById(R.id.cbx_app);
        }
    }
}
