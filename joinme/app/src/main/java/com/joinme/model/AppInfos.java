package com.joinme.model;

import android.graphics.drawable.Drawable;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AppInfos {
    private Drawable icon;
    private String label;
    private String usingTime;
    private int ifBanned ;
    public AppInfos(String usingTime,String lable){
        this.usingTime = usingTime;
        this.label = lable;
    }
    public AppInfos(Drawable icon, String lable) {
        this.icon = icon;
        this.label = lable;
    }

    public AppInfos(String lable,int ifBanned){
        this.label = lable;
        this.ifBanned  = ifBanned;
    }
    public Drawable getIcon() {
        return icon;
    }

    public String getUsingTime() {
        return usingTime;
    }

    public String getLabel() {
        return label;
    }

    public int getIfBanned(){
        return ifBanned;
    }

}
