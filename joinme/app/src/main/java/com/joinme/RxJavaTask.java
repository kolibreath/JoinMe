package com.joinme;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RxJavaTask {

    private int milliscends;
    private Action1 action1;
    private Subscription mSubscription;
    public RxJavaTask(int milliscends,Action1 action1){
        this.milliscends = milliscends;
        this.action1     = action1;
    }

    public void start(){
        if(mSubscription!=null||mSubscription.isUnsubscribed()){
            mSubscription = Observable.interval(milliscends, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(action1);
        }
    }

    public void stop(){
        if(mSubscription!=null&&mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
