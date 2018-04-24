package com.joinme.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joinme.RxTask;
import com.joinme.utils.RogueUtils;

import rx.Subscriber;

/**
 * Created by kolibreath on 17-10-21.
 */

public class QueryActivityService extends Service {

    private RxTask counterTask = new RxTask(1000, new Subscriber() {
        @Override
        public void onCompleted() { }
        @Override
        public void onError(Throwable e) {e.printStackTrace();}
        @Override
        public void onNext(Object o) {
            Log.d("fuck", "onNext: rogue");
            RogueUtils.excuteScreenLocker("ScreenSaverActivity"); }
    });

//    private MyTimer timer = new MyTimer(QUERY_COUNTDOWN,QUERY_INTERVAL);
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new QueryBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        counterTask.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class QueryBinder extends Binder {

    }
}
