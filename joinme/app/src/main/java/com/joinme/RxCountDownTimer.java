package com.joinme;

//import java.util.Observable;

import java.util.concurrent.TimeUnit;

import rx.Observable;

//RxJava的倒计时
public class RxCountDownTimer {

    //因为 time作为final 之后不能修改 所以扩展一个外部变量
    private int countdown;
    private int realCountdown;

    public Observable countdown(int time){
        if(time<0)
            time = 0;

        countdown  = time;
        return Observable
                .interval(0,1
                        , TimeUnit.SECONDS)
                .map(increaseTime -> {
                    realCountdown = countdown - increaseTime.intValue();
                    return realCountdown;
                })
                .take(countdown + 1);
    }

    public int getCountdown(){
        return realCountdown;
    }
}
