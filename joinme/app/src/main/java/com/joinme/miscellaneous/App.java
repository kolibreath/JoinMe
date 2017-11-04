package com.joinme.miscellaneous;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolibreath on 17-10-21.
 */

public class App extends Application{
    public static Context sContext;
    public static String sAppPackagename;
    public static String HOST = "39.108.79.110:5001";
    //类似于淘口令的开启口令
    public static String sPassWord = "我在这里学习赶快和我一起来学习吧";
    public static Context getContext(){
        return sContext;
    }
    public static String otherUserId ;
    public static List<String> blackListApps = new ArrayList<>();
    public static int UNLOCKTIMES = 0;
    public static List<String> pylist =  new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sAppPackagename = sContext.getPackageName();

        pylist.add("我的世界");
        pylist.add("哔哩哔哩概念");
        pylist.add("腾讯动漫");
        pylist.add("布卡漫画");
        pylist.add("Tim");
        pylist.add("人人视频");
        pylist.add("钉钉");
        pylist.add("微博");
        pylist.add("淘宝");
        pylist.add("系统桌面");
    }
}