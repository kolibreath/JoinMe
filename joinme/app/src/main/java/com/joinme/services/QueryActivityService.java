package com.joinme.services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joinme.miscellaneous.MyTimer;
import com.joinme.utils.RogueUtils;

/**
 * Created by kolibreath on 17-10-21.
 */

public class QueryActivityService extends Service {
    private static final long QUERY_COUNTDOWN = 3000;
    private static final long QUERY_INTERVAL = 1000;
    //默认打开这个应用
    private MyTimer.TimerListener countDownListener = new MyTimer.TimerListener() {
        @Override
        public void onTimerTick() {
            Log.d("rogue", "onTimerTick: ");
        }

        @Override
        public void onTimerFinish() {
            RogueUtils.excuteScreenLocker("ScreenSaverActivity");
        }
    };
    private MyTimer timer = new MyTimer(QUERY_COUNTDOWN,QUERY_INTERVAL);
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
        timer.setTimerListener(countDownListener);
        timer.start();
        timer.onFinish();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class QueryBinder extends Binder {

    }
}
