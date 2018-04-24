package com.joinme.utils;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.joinme.miscellaneous.App;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kolibreath on 17-10-21.
 */


//如何使你的应用变得流氓的工具
public class RogueUtils {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startRiot(List<String> list){
        rx.Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @Override
                    public void onNext(Object o) {
                        //list 中间包含的是被ban掉的app nam
                        String appLabel = AppInfoUtils.getApplicationLable(AppInfoUtils.getRunningApp());
                        Log.d("fuck", "appname "+appLabel);
                        if(!list.contains(appLabel)){
                            Intent i1 = new Intent(Intent.ACTION_VIEW);

                            i1.setClassName(App.sAppPackagename,
                                    App.sAppPackagename+"."+ "ScreenSaverActivity");
                            App.getContext().startActivity(i1);
                        }
                    }
                });

    }

    public static void excuteScreenLocker(String activityName){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setClassName(App.sAppPackagename,
                App.sAppPackagename+"."+activityName);
        App.getContext().startActivity(i);
    }
}
