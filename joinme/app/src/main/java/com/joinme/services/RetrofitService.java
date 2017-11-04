package com.joinme.services;

import com.joinme.model.AccepterInfo;
import com.joinme.model.AppPick;
import com.joinme.model.AppPicker;
import com.joinme.model.HotAppList;
import com.joinme.model.RaiserInfo;
import com.joinme.model.Status;
import com.joinme.model.UserId;
import com.joinme.model.UserMarkingCode;
import com.joinme.model.UserRecord;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kolibreath on 17-10-21.
 */

public interface RetrofitService {
    String HOST = "http://39.108.79.110:5001";
    //静默注册
    @POST("/api/signup/")
    Observable<UserMarkingCode> postRegister(@Body UserMarkingCode code);

    //获取app列表
    @POST("/api/hotapps/")
    Observable<HotAppList> postHotAppList(@Body String json);

    //发起一起学习
    @POST("/api/raiseit/")
    Observable<Status> postRaise(@Body RaiserInfo info) ;

    //接受约定`
    @POST("/api/acceptit/")
    Observable<Status> postAccept(@Body AccepterInfo info);

    //检查约定是否有效
    @POST("/api/check/")
    Observable<Status> postCheckIsAccepted(@Body RaiserInfo info);

    //获取对方修改/选择过了的列表
    @POST("/api/getapps/")
    Observable<AppPicker> postGetModifiedAppList(@Body UserId userId);

    //获取对方在学习过程中的使用手机的情况
    @POST("/api/getrecord/")
    Observable<UserRecord> postGetUserRecord(@Body UserId userId);

    //添加App列表
    @POST("/api/addapps/")
    Observable<Status> postTranslist(@Body AppPick appPick);

}
