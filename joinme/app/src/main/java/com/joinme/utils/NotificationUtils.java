package com.joinme.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by kolibreath on 17-10-21.
 */

//使用：先设置->show

public class NotificationUtils {
    private static String contents[] = {};
    private static String titles[]   = {};
    private static Class sTarget;
    public static Context sContext;
    private static int sNotifyId = 100;
    public static void setNotificationCompat(Context context,Class target ){
        sContext = context;
        sTarget = target;
    }
    public void show(int statusT,int statusC){
        //设置播放音乐
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(sContext)
                .setPriority( Notification.PRIORITY_MAX )
                .setSmallIcon(100)
                .setContentTitle(titles[statusT])
                .setContentText(contents[statusC])
                .setSound(soundUri)
                .setLights(0xFF0000, 200, 200)
                .setOngoing(true);
        Intent intent = new Intent(sContext,sTarget);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                sContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(sNotifyId,notification);
    }
}
