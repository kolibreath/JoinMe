package com.joinme;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

//RxJava 的輪讯器
public class RxTask {

    //interval
    private int milliscends;
    private Subscriber action1;
    private Subscription mSubscription= new Subscription() {
        boolean isUnsubscribed = false;
        @Override
        public void unsubscribe() {
            isUnsubscribed = true;}

        @Override
        public boolean isUnsubscribed() {
            return isUnsubscribed;
        }
    };
    public   RxTask(int milliscends, Subscriber action1){
        this.milliscends = milliscends;
        this.action1     = action1;
    }

    public void start(){
        if(mSubscription!=null||mSubscription.isUnsubscribed()){
            mSubscription = Observable.interval(milliscends, TimeUnit.MILLISECONDS)
                    .filter(aLong -> !mSubscription.isUnsubscribed())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(action1);
        }
    }

    public void stop(){
        if(mSubscription!=null&&mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
