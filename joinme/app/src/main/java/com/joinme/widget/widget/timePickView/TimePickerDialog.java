package com.joinme.widget.widget.timePickView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.joinme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kolibreath on 17-10-21.
 */

public class
TimePickerDialog extends BottomDialogFragment {
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.np_study_time)
    TimePickerView mTimePicker;
    OnPositiveButtonClickListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_study_time,null);
        ButterKnife.bind(this,view);

        final Dialog dialog = createBottomDialog(view);
        mTimePicker.setOnPickListener(new TimePickerView.OnPickListener() {
            @Override
            public void onPick(int hour, int minute) {
               // mTvsetTime.setText(hour+"时"+minute+"分");
            }
        });
        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onPositiveButtonListener(mTimePicker.getHour()
                    ,mTimePicker.getMinute());
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static TimePickerDialog newInstance(int  hour, int minute){
        Bundle args = new Bundle();
        args.putInt("hour",hour);
        args.putInt("minute",minute);
        TimePickerDialog fragment = new TimePickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener listener){
        this.listener = listener;
    }
    public interface OnPositiveButtonClickListener{
        void onPositiveButtonListener(int hour, int minute);
    }
}
