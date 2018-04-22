package com.joinme.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by kolibreath on 17-10-21.
 */

public class SnackBarUtils {
    //常用的一个实现
    public static View.OnClickListener listenerImpl = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

        }
    };
    public static void showShort(View view,String message){
       Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

    public static void showLong(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }

    public static void showWithAction(View view,String message){
        Snackbar.make(view,message,Snackbar.LENGTH_INDEFINITE)
        .setAction(message,listenerImpl).show();
    }
}
