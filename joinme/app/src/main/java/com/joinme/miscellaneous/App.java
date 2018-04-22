package com.joinme.miscellaneous;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * Created by kolibreath on 17-10-21.
 */

public class App extends Application{
    public static Context sContext;
    public static String sAppPackagename;
    //类似于淘口令的开启口令
    public static String sPassWord = "我在这里学习赶快和我一起来学习吧";

    public static Context getContext(){
        return sContext;
    }
    public static String otherUserId,userId;
    public static int UNLOCKTIMES = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sAppPackagename = sContext.getPackageName();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}