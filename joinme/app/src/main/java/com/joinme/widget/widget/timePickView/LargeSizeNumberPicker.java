package com.joinme.widget.widget.timePickView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.joinme.R;
/**
 * Created by kolibreath on 17-10-21.
 */

public class LargeSizeNumberPicker extends NumberPicker {
    public LargeSizeNumberPicker(Context context) {
        this(context,null);
    }

    public LargeSizeNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setWrapSelectorWheel(false);
        NumberPickerHelper.setDividerColor(this, Color.TRANSPARENT);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view){
        if(view instanceof EditText){
            ((EditText)view).setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            ((EditText)view).setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
