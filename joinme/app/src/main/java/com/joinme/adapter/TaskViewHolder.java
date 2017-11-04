package com.joinme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joinme.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    public TextView mTaskName;

    public TaskViewHolder(View itemView) {
        super(itemView);
        mTaskName = (TextView) itemView.findViewById(R.id.txv_task_name);
    }
}
