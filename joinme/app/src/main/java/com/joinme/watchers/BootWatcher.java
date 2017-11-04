package com.muxistudio.keygruardtest.watchers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

//需要祖册为静态广播
    //http://www.jianshu.com/p/e6a6cf2c3efe
public class BootWatcher {
    private BootListener listener;
    private Context context;
    private BootReceiver receiver;
    public  String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    public BootWatcher(Context context){
        this.context = context;
        receiver = new BootReceiver();
    }

    public void register(BootListener listener){
        if(listener!=null){
            this.listener = listener;
        }
        if(receiver!=null){
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_BOOT);
            context.registerReceiver(receiver,filter);
        }
    }

    public void unRegister(){
        if(receiver!=null){
            context.unregisterReceiver(receiver);
        }
    }

    public class BootReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            listener.onBoot();
        }
    }

    public interface  BootListener{
        void onBoot();
    }
}
