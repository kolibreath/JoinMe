package com.joinme.model;

import java.util.List;

/**
 * Created by kolibreath on 17-10-22.
 */

public class AppPick {

    /**
     * id : ffd
     * ifmodify : 1
     * apps : ["appname1","appname2"]
     */

    private String id;
    private int ifmodify;
    private List<String> apps;

    public AppPick(String id, int ifmodify, List<String> apps) {
        this.id = id;
        this.ifmodify = ifmodify;
        this.apps = apps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIfmodify() {
        return ifmodify;
    }

    public void setIfmodify(int ifmodify) {
        this.ifmodify = ifmodify;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }
}