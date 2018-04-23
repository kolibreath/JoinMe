package com.joinme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.joinme.miscellaneous.App;
import com.joinme.model.AccepterInfo;
import com.joinme.model.AppPick;
import com.joinme.model.NetWorkUtils;
import com.joinme.model.Password;
import com.joinme.model.RaiserInfo;
import com.joinme.model.Status;
import com.joinme.model.UserMarkingCode;
import com.joinme.services.RxFactory;
import com.joinme.utils.AlertDialogUtils;
import com.joinme.utils.AppInfoUtils;
import com.joinme.utils.ClipBoardUtils;
import com.joinme.utils.PreferenceUtils;
import com.joinme.utils.SnackBarUtils;
import com.joinme.utils.UserMarkingUtils;
import com.joinme.utils.VibratorUtils;
import com.joinme.widget.widget.timePickView.TimePickerDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private int mPickMinute, mPickHour;
    private int mInterval = 3000;

    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RxTask mCheckAccepted =  new RxTask(mInterval, o -> checkIsAccepted());
    @BindView(R.id.btn_raise_study)
    ImageButton mBtnRaiseStudy;
    @OnClick({R.id.btn_raise_study})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_raise_study:
                TimePickerDialog dialog = TimePickerDialog.newInstance(0, 0);
                dialog.show(getSupportFragmentManager(), "study_time_pick");
                //一旦确认开始　就会给服务器发送消息请求开始
                dialog.setOnPositiveButtonClickListener((hour, minute) -> {
                    raiseStudy(hour, minute);
                    mPickHour = hour;
                    mPickMinute = minute;
                });
                break;
        }
    }
    @OnLongClick({R.id.btn_raise_study})
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.btn_raise_study:
                VibratorUtils.vibrate(2000);
                break;
        }
        return false;
    }
    @Override
    protected void onRestart() {
        raiseAccept();
        super.onRestart();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AppInfoUtils.isSwitch();

        silentRegister();
        App.userId = PreferenceUtils.getString(PreferenceUtils.sUserMarker);

        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        requestPower();
    }


    public void requestPower() {
            //判断是否已经赋予权限
            for(int i=0;i<mPermissions.length;i++) {
                if (ContextCompat.checkSelfPermission(this, mPermissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            mPermissions[i])) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                    } else {
                        //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                        ActivityCompat.requestPermissions(this,
                               mPermissions, 1);
                    }
                }
            }
    }
    //发起者和接受者 静默注册
    private void silentRegister(){
        String temp = PreferenceUtils.getString(PreferenceUtils.sUserMarker);
        if(!TextUtils.isEmpty(temp)){
            return;
        }
        String userMarker = UserMarkingUtils.getUserMaker();
            RxFactory.getRetrofitService()
                .postRegister(new UserMarkingCode(userMarker))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userMarkingCode -> {
                    App.userId = userMarkingCode.getIdcode();
                    PreferenceUtils.saveString(PreferenceUtils.sUserMarker,
                            App.userId);
                },Throwable::printStackTrace,()->{});
    }
    //发起者发起一个学习
    private void raiseStudy(int hour,int minute){
        //传输过去的是：xxx小时xxxx分钟
        String timeString = hour + "小时" + minute + "分钟";
        String raiserId = App.userId;
        Password password = new Password(raiserId,timeString);
        ClipBoardUtils.copy(password.toString());
        SnackBarUtils.showShort(mBtnRaiseStudy,"学习口令已经复制到你的剪贴板");
        RxFactory.getRetrofitService()
                .postRaise(new RaiserInfo(raiserId, timeString))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                   mCheckAccepted.start();
                    },Throwable::printStackTrace,()->{});
    }
    //接受者检查口令 然后表示接受
    //一旦接受之后就会进入ScreenSaver
    //todo 这一整个逻辑有问题嘛？
    private void raiseAccept() {
        String message = ClipBoardUtils.getText();
        if (TextUtils.isEmpty(message)||!message.contains(Password.part1)
                ||!message.contains(Password.part2))
            return;
        //如果是接受者的话,这个就是接受者的id
        String raiseInfo[] = message.split(",");
        String raiserId     = raiseInfo[1].substring(2,raiseInfo[1].length());
        App.otherUserId     = raiserId;
        //如果接受者的id和raiserId 相同说明是同一个人
        if(App.userId.equals(raiserId))
            return;

        AlertDialogUtils.show(MainActivity.this,message);
        RxFactory.getRetrofitService()
                .postAccept(new AccepterInfo(raiserId,App.userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        status -> {
                            transAppList();
                },Throwable::printStackTrace,()->{});}

    //发起者检查接受者是否接受 适用于发起者发起学习之后
    private void checkIsAccepted(){
        String userMarker = App.userId;
        RxFactory.getRetrofitService()
                .postCheckIsAccepted(new RaiserInfo(userMarker))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(status -> {
                            mCheckAccepted.stop();
                            transAppList();
                        },e->{int code = NetWorkUtils.getresponseCode(e);
                            if(code==404){
                                return; }},()->{});

    }

    private void transAppList(){
        List<String> appLabelList  =
                AppInfoUtils.getAppNames(AppInfoUtils.getAppInfos());
        RxFactory.getRetrofitService()
                .postTranslist(new AppPick(App.userId,
                        0,appLabelList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onNext(Status status) {
                        //todo
//                        mIsAddedTask.start();
                        //在两个人都传输完成之后再来回调
                        ScreenSaverActivity.
                                start(MainActivity.this, mPickHour, mPickMinute);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_close:
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
