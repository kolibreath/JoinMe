package com.joinme.utils;

/**
 * Created by kolibreath on 17-10-21.
 */

//使用说明：直接使用
public class UserMarkingUtils {
 public static String getUserMaker(){
     return new String(String.valueOf(System.currentTimeMillis())
             .substring(1,9));
 }
}
