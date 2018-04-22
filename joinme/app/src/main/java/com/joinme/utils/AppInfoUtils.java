package com.joinme.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.joinme.R;
import com.joinme.miscellaneous.App;
import com.joinme.model.AppInfos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by kolibreath on 17-10-21.
 */

/*
需要在清单之中添加
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
            tools:ignore="ProtectedPermissions"/>
 */

public class  AppInfoUtils {

    private static int sAgo = 30;

    //获取应用的包名 icon drawable 等等
    public static List<AppInfos> getAppInfos() {
        Context sContext = App.getContext();
        List<AppInfos> appInfosList = new ArrayList<>();
        PackageManager packageManager = sContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo pinfo : packageInfoList) {
            if ((pinfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String name = packageManager.getApplicationLabel(pinfo.applicationInfo).toString();
                if (!name.equals(R.string.app_name)) {
                    Drawable appIcon = pinfo.applicationInfo.loadIcon(packageManager);
                    AppInfos infos = new AppInfos(appIcon, name);
                    appInfosList.add(infos);
                }
            }
        }
        return appInfosList;
    }

    public static List<String> getAppNames(List<AppInfos> infoList){
        List<String> names= new ArrayList<>();
        for(int i=0;i<infoList.size();i++){
            names.add(infoList.get(i).getLabel());
        }
        return  names;
    }
    //返回的是应用的包名
    //返回最近的应用的包名
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getRunningApp(){
        String topPackagename;
        Context sContext = App.getContext();
            UsageStatsManager mManager = (UsageStatsManager) sContext.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY
            ,time - 1000*sAgo,time);
            if(stats!=null){
                SortedMap<Long,UsageStats> mSortmap = new TreeMap<>();
                for(UsageStats usageStats:stats){
                    String temp = App.sAppPackagename;
                    if(!temp.equals(usageStats.getPackageName()))
                        mSortmap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if(!mSortmap.isEmpty()){
                    topPackagename = mSortmap.get(mSortmap.lastKey()
                    ).getPackageName();
                    return topPackagename;
                }else{
                    return null;
                }
            }else{
                return null;
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isSwitch(){
        Context context = App.getContext();
        long ts  = System.currentTimeMillis();
        UsageStatsManager manager =
                (UsageStatsManager) context.getSystemService("usagestats");
        List<UsageStats> queryUsageStats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                0,ts);
        if(queryUsageStats==null||queryUsageStats.isEmpty()){
            App.getContext().startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            return false;
        }else{
            return true;
        }
    }

    //返回应用使用的时长 和 相关信息等
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AppInfos> getAppUsingData() {
        List<AppInfos> list = new ArrayList<>();
        Calendar begin = Calendar.getInstance();
        begin.add(Calendar.HOUR_OF_DAY, -1);
        Calendar end = Calendar.getInstance();
        UsageStatsManager manager = (UsageStatsManager) App.getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, begin.getTimeInMillis(),
                end.getTimeInMillis());
        for (UsageStats us : stats) {
            PackageManager packageManager = App.getContext().getPackageManager();
            try {
                ApplicationInfo info = packageManager.getApplicationInfo
                        (us.getPackageName(),PackageManager.GET_META_DATA);
                if((info.flags&info.FLAG_SYSTEM)<=0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    String time = simpleDateFormat.format(new Date(us.getLastTimeUsed()));
                    String lable = (String) packageManager.getApplicationLabel(info);
                    
                    list.add(new AppInfos(time,lable));
                }
                } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String getApplicationLable(String packageName){
        String label = null;
        PackageManager manager  = App.getContext().getPackageManager();
        try {
            label = manager.getApplicationLabel(manager.
                    getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return label;
    }

}
