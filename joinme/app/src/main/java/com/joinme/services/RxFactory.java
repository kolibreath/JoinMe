package com.joinme.services;
/**
 * Created by kolibreath on 17-10-21.
 */

public class RxFactory {
    public volatile static RetrofitService sRetrofitService = null;

    public static RetrofitService getRetrofitService(){
        RetrofitService retrofitService = sRetrofitService;
        if (retrofitService == null){
            synchronized (RxFactory.class){
                retrofitService = sRetrofitService;
                if (retrofitService == null) {
                    sRetrofitService = new RxRetrofit().getRetrofitService();
                    retrofitService = sRetrofitService;
                }
            }
        }
        return retrofitService;
    }

}
