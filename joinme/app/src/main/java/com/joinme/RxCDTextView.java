package com.joinme;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import rx.functions.Action1;

//自定义一个可以倒计时的textView
public class RxCDTextView extends TextView{


    public RxCDTextView(Context context){
        super(context);
    }

    public RxCDTextView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  RxCDTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //这里获取到的是秒数 所以 之后需要把countdown解析一下
    public void startCountdown(int time){
        RxCountDownTimer timer = new RxCountDownTimer();
        timer.countdown(time)
                .subscribe(o ->
                        RxCDTextView.this.setText(timer.getCountdown()));
    }
}
