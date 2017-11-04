package com.joinme.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joinme.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context mContext;

    public TaskAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, null);
        TaskViewHolder taskViewHolder = new TaskViewHolder(layoutView);
        return taskViewHolder;
    }


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        for (int i = 0; i < 2; i++) {
            holder.mTaskName.setText("Task" + i);
        }
        if (position % 3 == 0) {
            holder.itemView.setBackgroundColor(Color.BLACK);
        } else if (position % 3 == 1) {
            holder.itemView.setBackgroundColor(Color.RED);
        } else if (position % 3 == 2) {
            holder.itemView.setBackgroundColor(Color.DKGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
