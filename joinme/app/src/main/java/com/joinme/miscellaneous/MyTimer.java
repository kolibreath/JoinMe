package com.joinme.miscellaneous;

import android.os.CountDownTimer;

/**
 * Created by kolibreath on 17-10-21.
 */

public class MyTimer extends CountDownTimer {
    public TimerListener timerListener = null;
    private long millisInFuture;
    private long countDownInterval;
    public static int STANDARD_INTERVAL = 1000;
    public MyTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
    }
    @Override
    public void onTick(long l) {
        if(timerListener!=null){
            timerListener.onTimerTick();
        }
    }
    @Override
    public void onFinish() {
        if(timerListener!=null){
            timerListener.onTimerFinish();
        }
    }

    public void setTimerListener(TimerListener listener){
        timerListener = listener;
    }
    public interface TimerListener{
        void onTimerTick();
        void onTimerFinish();
    }
    public String getText(long millisInFuture){
      long secondsInFuture = millisInFuture/1000;
        int seconds = (int) (secondsInFuture%60);
        int minutes = (int) (secondsInFuture/60);
        return String.format("%2d:%2d",minutes,seconds);
    }
    public long getMillisInFuture() {
        return millisInFuture;
    }

    public long getCountDownInterval() {
        return countDownInterval;
    }
}

