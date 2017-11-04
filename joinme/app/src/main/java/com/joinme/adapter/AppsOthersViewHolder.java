package com.joinme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;

import com.joinme.R;

/**
 * Created by lwxwl on 2017/10/21.
 */

public class AppsOthersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mAppOthersName;
    public CheckBox mIsBanned;

    public AppsOthersViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mAppOthersName = (TextView) itemView.findViewById(R.id.txv_app_others_name);
        mIsBanned = (CheckBox) itemView.findViewById(R.id.cbx_app);
    }

    @Override
    public void onClick(View view) {

    }
}
