package com.joinme.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.joinme.miscellaneous.App;


/**
 * Created by ybao on 16/4/19.
 * SharedPreferences存储类
 */
public class PreferenceUtils {

    public static String sUserMarker = "sUserMarker";
    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean def) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getBoolean(key, def);
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getString(key, "");
    }


    public static String getString(String key, String def) {
        return PreferenceManager.getDefaultSharedPreferences(App.sContext).getString(key, def);
    }


    //可用于用户上次使用后注销账号时移除账号
    public static void clearString(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.remove(key);
        editor.apply();
    }


    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getFloat(key, -1);
    }

    public static void clearFloat(String key){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.sContext).edit();
        editor.remove(key);
        editor.apply();

    }

    public static long getLong(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getLong(key, -1);
    }

    public static int getInt(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getInt(key, -1);
    }

    public static int getInt(String key, int def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        return sp.getInt(key, def);
    }

    /**
     * 清楚所有的数据,在注销时使用
     */
    public void clearAllData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.sContext);
    }


}
