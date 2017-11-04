package com.joinme.utils;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import com.joinme.miscellaneous.App;

/**
 * Created by kolibreath on 17-10-21.
 */

public class ClipBoardUtils {
    //使用：直接类名.方法名
    public static void copy(String content){
        Context context = App.getContext();
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(content);
    }
    public static void  paste(String target){
        Context context = App.getContext();
        ClipboardManager manager =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        target = manager.getText().toString();
    }

    public static String getText(){
        Context c = App.getContext();
        ClipboardManager manager =
                (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        if(manager.getText()==null)
            return null;
        String temp = manager.getText().toString();
        return temp;
    }
    public static void clear(){
        Context c = App.getContext();
        ClipboardManager manager = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText("");
    }
    //口令检测然后确定是否打开确定的Activity
    //在Activity的onCreate或者onRestart中打开检测
    public static void openStudyConnection(Context context,Context activity){
        Context c = App.getContext();
        ClipboardManager manager =
                (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        String temp = manager.getText().toString();
        if(temp.contains(App.sPassWord)){
            Intent intent = new Intent(context,activity.getClass());
            context.startActivity(intent);
        }
    }
}
