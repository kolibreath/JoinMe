package com.joinme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joinme.R;

/**
 * Created by lwxwl on 2017/10/21.
 */

public class AppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mAppLogo;
    public TextView mAppName;
    public ImageView mAppBanned;

    public AppsViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mAppLogo = (ImageView) itemView.findViewById(R.id.imv_app_logo);
        mAppName = (TextView) itemView.findViewById(R.id.txv_app_name);
        mAppBanned = (ImageView) itemView.findViewById(R.id.imv_app_banned);
    }

    @Override
    public void onClick(View view) {
        mAppBanned.setVisibility(View.VISIBLE);
        mAppLogo.setAlpha(0.5f);
    }
}
