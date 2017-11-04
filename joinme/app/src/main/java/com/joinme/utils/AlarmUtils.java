package com.joinme.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joinme.miscellaneous.App;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AlarmUtils {
    //如果需要自启动某个程序的话只需要输入这个程序的报名

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setAlarm(Calendar calendar, Intent intent) {
        Context context = App.getContext();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent  = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC, calendar.getTimeInMillis() ,pIntent);
    }
}