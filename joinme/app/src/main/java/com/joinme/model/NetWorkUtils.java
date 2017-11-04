package com.joinme.model;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by kolibreath on 17-10-21.
 */

public class NetWorkUtils {
    public static int getresponseCode(Throwable e){
        int code = 0;
        if(e instanceof HttpException) {
            //获取对应statusCode和Message
            HttpException exception = (HttpException) e;
            String message = exception.response().message();
            code = exception.response().code();
            return code;
        }
        return code;
    }
}
