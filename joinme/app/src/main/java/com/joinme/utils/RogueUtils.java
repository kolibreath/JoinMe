package com.joinme.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joinme.miscellaneous.App;
import com.joinme.services.QueryActivityService;

import java.util.List;

/**
 * Created by kolibreath on 17-10-21.
 */


//如何使你的应用变得流氓的工具
public class RogueUtils {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startRiot(Context context, List<String> list){
        for (int i = 0; i <list.size() ; i++) {
            String packageName = AppInfoUtils.getRunningApp();
            String label = AppInfoUtils.getApplicationLable(packageName);
            if(list.contains(label)) {
                Intent intent = new Intent(context, QueryActivityService.class);
                context.startService(intent);

                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setClassName(App.sAppPackagename,App.sAppPackagename+"."+ "ScreenSaverActivity");
                App.getContext().startActivity(i1);
            break;
        }
        }
    }

    public static void excuteScreenLocker(String activityName){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setClassName(App.sAppPackagename,
                App.sAppPackagename+"."+activityName);
        App.getContext().startActivity(i);
    }
}
