package com.joinme;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.joinme.miscellaneous.App;
import com.joinme.model.AccepterInfo;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
View.OnLongClickListener{
    private boolean isAccepted = false;
    private int pickMinute,pickHour ;
    private CountDownTimer checkAcceptedTimer = new CountDownTimer(60000,2000) {
        @Override
        public void onTick(long l) {
            if (!isAccepted){
                checkIsAccepted();
            }else{
                checkAcceptedTimer.cancel();
            }
        }
        @Override
        public void onFinish() {
        }
    };
    @BindView(R.id.btn_raise_study)
    ImageButton mBtnRaiseStudy;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_raise_study:
                TimePickerDialog dialog = TimePickerDialog.newInstance(0, 0);
                dialog.show(getSupportFragmentManager(), "study_time_pick");
                //一旦确认开始　就会给服务器发送消息请求开始
                dialog.setOnPositiveButtonClickListener(new TimePickerDialog.OnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonListener(int hour, int minute) {
                        raiseStudy(hour,minute);
                        pickHour = hour;
                        pickMinute = minute;
                    }
                });
                break;
        }
    }

    @Override
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

        String name = AppInfoUtils.getRunningApp();
        mBtnRaiseStudy.setOnClickListener(this);
        mBtnRaiseStudy.setOnLongClickListener(this);

        silentRegister();
        raiseAccept();
    }
    //发起者和接受者 静默注册
    private void silentRegister(){
        String temp = PreferenceUtils.getString(R.string.user_marker);
        if(!temp.equals("NOTHING")||temp==null){
            return;
        }
        String userMarker = UserMarkingUtils.getUserMaker();
        RxFactory.getRetrofitService()
                .postRegister(new UserMarkingCode(userMarker))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserMarkingCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserMarkingCode code) {
                        PreferenceUtils.putString(R.string.user_marker
                                ,code.getIdcode());
                    }
                });
    }
    //发起者发起一个学习
    private void raiseStudy(int hour,int minute){
        //传输过去的是：xxx小时xxxx分钟
        final String timeString = hour + "小时" + minute + "分钟";
        final String raiserId = PreferenceUtils.getString(R.string.user_marker);
        RxFactory.getRetrofitService()
                .postRaise(new RaiserInfo(raiserId, timeString))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Status status) {
                        Password password = new Password(raiserId,timeString);
                        ClipBoardUtils.copy(password.toString());
                        Toast.makeText(MainActivity.this, "学习口令已经复制到你的剪贴板",
                                Toast.LENGTH_SHORT).show();
                        SnackBarUtils.showShort(mBtnRaiseStudy
                                ,"学习口令已经复制到你的剪贴板");
                        checkAcceptedTimer.start();
                    }
                });
    }
    //接受者检查口令 然后表示接受
    //一旦接受之后就会进入ScreenSaver
    private void raiseAccept() {
        String message = ClipBoardUtils.getText();
        if (message==null||!message.contains(Password.part1)
                ||!message.contains(Password.part2))
            return;
        //如果是接受者的话,这个就是接受者的id
        String userMarker = PreferenceUtils.getString(R.string.user_marker);
        String t1 = message.replace(Password.part1,"");
        String t2 = message.replace(Password.part2,",");
        String raiseInfo[] = message.split(",");
        String raiserTimer  = raiseInfo[0];
        String raiserId     = raiseInfo[1].substring(2,raiseInfo[1].length());
        App.otherUserId     = raiserId;
        //如果接受者的id和raiserId 相同说明是同一个人
        if(userMarker.equals(raiserId))
            return;
        AlertDialogUtils.show(MainActivity.this,message);
        RxFactory.getRetrofitService()
                .postAccept(new AccepterInfo(raiserId,userMarker))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Status status) {
                        ScreenSaverActivity.start(MainActivity.this);
                    }
                });
    }
    //发起者检查接受者是否接受 适用于发起者发起学习之后
    private void checkIsAccepted(){
        String userMarker = PreferenceUtils.getString(R.string.user_marker);
        RxFactory.getRetrofitService()
                .postCheckIsAccepted(new RaiserInfo(userMarker))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Status>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                int code = NetWorkUtils.getresponseCode(e);
                                if(code==404){
                                    return;
                                }
                            }
                            @Override
                            public void onNext(Status status) {
                                isAccepted = true;
                                checkAcceptedTimer.cancel();
                                ScreenSaverActivity.start(MainActivity.this,
                                        pickHour,pickMinute);
                            }
                        });
            }
}
