package com.joinme.utils;

import android.content.Context;
import android.os.Vibrator;

import com.joinme.miscellaneous.App;

/**
 * Created by kolibreath on 17-10-21.
 */

public class VibratorUtils {
    public static void vibrate(long millis){
        Context context = App.getContext();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(millis);
    }
    public static void vibrate(long millis,long pause,int repeat){
        Context context = App.getContext();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0,millis,pause},repeat);
    }
}
