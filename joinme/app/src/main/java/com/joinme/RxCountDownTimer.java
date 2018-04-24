package com.joinme;

//import java.util.Observable;

import java.util.concurrent.TimeUnit;

import rx.Observable;

//RxJava的倒计时
public class RxCountDownTimer {

    //因为 time作为final 之后不能修改 所以扩展一个外部变量
    private int countdown;
    //剩余的秒数
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
                //发送　倒计时一个一个的observable
                .take(countdown + 1);
    }

    public Observable onFinish(int time){
        int countdown = time;

        return Observable
                .interval(0,1,TimeUnit.SECONDS)
//                .toSingle()
                .map(increseTime-> countdown - increseTime.intValue())
                .filter(integer -> integer<=0).first();
    }

    public int getCountdown(){
        return realCountdown;
    }
}
