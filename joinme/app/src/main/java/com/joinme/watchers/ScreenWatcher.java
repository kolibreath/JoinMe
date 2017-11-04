package com.joinme.watchers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by kolibreath on 17-10-21.
 */


//只需要动态注册就可以
public class ScreenWatcher {
    private ScreenListener listener;
    private Context context;
    private UnlockScreenReceiver receiver;
    public ScreenWatcher(Context context){
        this.context = context;
        receiver = new UnlockScreenReceiver();
    }
    public void register(ScreenListener listener){
        if(listener!=null){
            this.listener = listener;
        }
        if(receiver!=null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            context.registerReceiver(receiver, filter);
        }
    }

    public void unregister(){
        if(receiver!=null){
            context.unregisterReceiver(receiver);
        }
    }

    private class UnlockScreenReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_SCREEN_ON)){
                listener.onUnlock();
            }
        }
    }

    public interface ScreenListener {
        void onUnlock();
    }
}
