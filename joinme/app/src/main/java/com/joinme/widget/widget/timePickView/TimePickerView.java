package com.joinme.widget.widget.timePickView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.joinme.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kolibreath on 17-10-21.
 */



public class TimePickerView extends LinearLayout {

    //设置学习倒计时的通用listener
    int resId;
    @BindView(R.id.np_minute)
    NumberPicker mMinutesPicker;
    @BindView(R.id.np_hour)
    NumberPicker mHoursPicker;
    public static String[] sMinutes= new String[60];
    public static String[] sHours  = new String[12];
    public OnPickListener onPickListener;
    public TimePickerView(Context context) {
        this(context,null);
    }
    public TimePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context,R.layout.view_time_picker, this);
        ButterKnife.bind(this,view);
        setWillNotDraw(false);
        initView();
    }
    public TimePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        NumberPickerHelper.setDividerColor(mMinutesPicker, Color.TRANSPARENT);
        NumberPickerHelper.setDividerColor(mHoursPicker, Color.TRANSPARENT);
        mMinutesPicker.setMinValue(0);
        mMinutesPicker.setMaxValue(60);
        mHoursPicker.setMinValue(0);
        mHoursPicker.setMaxValue(11);
        initArrays();
        mMinutesPicker.setDisplayedValues(sMinutes);
        mHoursPicker.setDisplayedValues(sHours);

        mHoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old, int newVal) {
                if(onPickListener!=null)
                    onPickListener.onPick(newVal,mMinutesPicker.getValue());
            }
        });

        mMinutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old, int newVal) {
                if(onPickListener!=null)
                    onPickListener.onPick(mHoursPicker.getValue(),newVal);
            }
        });
    }
    private void initArrays(){
        for(int i=0;i<60;i++){
            sMinutes[i] = i +"分";
        }
        for(int i=0;i<12;i++){
            sHours[i] = i+"时";
        }
    }
    public void setOnPickListener(OnPickListener listener){
        onPickListener = listener;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NumberPickerHelper.drawVerticalPickerBg(canvas,getWidth(),getHeight());
    }
    public interface OnPickListener {
        void onPick(int hour,int minute);
    }

    public int getHour(){
        return mHoursPicker.getValue();
    }

    public int getMinute(){
        return mMinutesPicker.getValue();
    }
}
