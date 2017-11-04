package com.joinme.model;

/**
 * Created by kolibreath on 17-10-21.
 */

public class AppExchangeList {

    /**
     * id : 1
     * ifmodify : 1
     * apps : {"appname1":1}
     */

    private String id;
    private int ifmodify;
    private AppsBean apps;

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

    public AppsBean getApps() {
        return apps;
    }

    public void setApps(AppsBean apps) {
        this.apps = apps;
    }

    public static class AppsBean {
        /**
         * appname1 : 1
         */

        private int appname1;

        public int getAppname1() {
            return appname1;
        }

        public void setAppname1(int appname1) {
            this.appname1 = appname1;
        }
    }
}
