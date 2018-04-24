package com.joinme.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AlertDialogUtils {
    //在锁屏中出现复用性比较低
    public static void show(Context context, final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("你还在学习！！")
                .setMessage("去学习吧")
                .setCancelable(false)
                .setPositiveButton("还是算了吧", (dialogInterface, i) -> {
                    VibratorUtils.vibrate(3000);
                    SnackBarUtils.showShort(view,"我在学习在！");
                    dialogInterface.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void show(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("要来一起学习吗")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("接受", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ClipBoardUtils.clear();
                })
                .setNegativeButton("拒绝", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    ClipBoardUtils.clear();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void show(Context context,String message,OnPostiveListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("选择给别人禁用的App")
                .setCancelable(false)
                .setMessage(message)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("确定",listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public interface OnPostiveListener extends DialogInterface.OnClickListener{
        @Override
        void onClick(DialogInterface dialogInterface, int i);
    }
}
