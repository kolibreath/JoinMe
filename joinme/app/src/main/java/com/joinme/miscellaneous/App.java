package com.joinme.miscellaneous;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> mockList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sAppPackagename = sContext.getPackageName();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initMockList();
    }

    private  void initMockList(){
        mockList.add("腾讯动漫");
        mockList.add("百词斩");
        mockList.add("别踩白块儿");
        mockList.add("人人视频");
        mockList.add("知乎");
        mockList.add("系统用户界面");
    }
}