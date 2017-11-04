package com.joinme.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolibreath on 17-11-4.
 */

public class MyTextUtils {

    //用于处理返回的json格式的数据
    private static List<String> parseString(String json){
        String apps[] = json.split(",");
        List<String> applist = new ArrayList<>();
        for(int i=0;i<apps.length;i++){
            if(i==0){
                String temp = apps[i].substring(2,apps[i].length()-2);
                applist.add(temp);
            }
            if(i==apps.length-1){
                String temp = apps[i].substring(1,apps[i].length()-3);
                applist.add(temp);
            }
            String temp = apps[i].substring(2,apps[i].length()-1);
            applist.add(temp);
        }
        return applist;
    }
}
