package com.joinme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joinme.adapter.AppsAdapter;
import com.joinme.adapter.AppsOthersAdapter;
import com.joinme.miscellaneous.App;
import com.joinme.model.AppInfos;
import com.joinme.model.AppPick;
import com.joinme.model.AppPicker;
import com.joinme.model.NetWorkUtils;
import com.joinme.model.Status;
import com.joinme.model.UserId;
import com.joinme.model.UserRecord;
import com.joinme.services.RxFactory;
import com.joinme.utils.AlertDialogUtils;
import com.joinme.utils.AppInfoUtils;
import com.joinme.utils.PreferenceUtils;
import com.joinme.utils.RogueUtils;
import com.joinme.watchers.HomeWatcher;
import com.joinme.watchers.ScreenWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.waveloadingview.WaveLoadingView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Edited by kolibreath on 2017/11/5.
 */

public class ScreenSaverActivity extends AppCompatActivity {
    @BindView(R.id.txv_saver_time)
    TextView txvSaverTime;
    @BindView(R.id.btn_saver_pause)
    Button btnSaverPause;
    @BindView(R.id.btn_saver_end)
    Button btnSaverEnd;
    @BindView(R.id.wld_view)
    WaveLoadingView wldProgress;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;
    @BindView(R.id.bg_view)
    View mBgView;
    @OnClick({R.id.btn_enter})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_enter:
                isAddedTimer.cancel();
                isModifyTimer.cancel();
                isReceiveRecordTimer.cancel();

                mRecyclerView.setVisibility(View.GONE);
                mBgView.setVisibility(View.GONE);
                mBgView2.setVisibility(View.GONE);
                transModifiedList();
                mBgView.setVisibility(View.GONE);
                mBgView2.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                millisTime = (pickHour * 3600 + pickMinute * 60) * 1000;
                wldProgress.setAnimDuration(1000);
                wldProgress.startAnimation();
                //倒计时
                studyTimer.start();
                mHomeWatcher.startWatch();
                mHomeWatcher.setOnHomePressedListener(homePressedListener);
                break;
            case R.id.btn_other_enter:
                /*
                mBgView.setVisibility(View.GONE);
                mBgView2.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                long millis = (pickHour * 3600 + pickMinute * 60) * 1000;
                wldProgress.setAnimDuration(millis);
                //倒计时
                studyTimer = new MyTimer(millis, 1000);
                studyTimer.setTimerListener(timerListener);
                studyTimer.onTick(1000);
                studyTimer.onFinish();
                mHomeWatcher.startWatch();
                mHomeWatcher.setOnHomePressedListener(homePressedListener);
                */
                break;
            //case R.id.btn_ban_cancel:
            //    mRecyclerView.setVisibility(View.GONE);
             //   break;
        }
    }
    private AlertDialogUtils.OnPostiveListener otherListListener = new AlertDialogUtils.OnPostiveListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
    private int mTimerMarker = 0;
    private long millisTime;
    private boolean isFinal = false;
    private List<String> otherUserList = new ArrayList<>();
    private List<String> otherBlackList = new ArrayList<>();
    private AppsAdapter mAppsAdapter;
    private AppsOthersAdapter mAppotherAdapter;
    private HomeWatcher.OnHomePressedListener homePressedListener = new HomeWatcher.OnHomePressedListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onHomePressed() {
            RogueUtils.startRoit(ScreenSaverActivity.this,App.pylist);
        }

        @Override
        public void onHomeLongPressed() {
        }
    };
    private HomeWatcher mHomeWatcher = new HomeWatcher(ScreenSaverActivity.this);
    private ScreenWatcher mScreenWatcher = new ScreenWatcher(ScreenSaverActivity.this);
    private ScreenWatcher.ScreenListener mScreenListener = new ScreenWatcher.ScreenListener() {
        @Override
        public void onUnlock() {
            App.UNLOCKTIMES++;
            RogueUtils.excuteScreenLocker("ScreenSaverActivity");
        }
    };
    private boolean isAdded = false;
    private boolean isReceiveRecord = false;
    private CountDownTimer isModifyTimer = new CountDownTimer(millisTime, 200) {
        @Override
        public void onTick(long l) {
            if (!isFinal) {
                getFinalAppList();
            } else {
                isModifyTimer.cancel();
            }
        }

        @Override
        public void onFinish() {
        }
    };
    private CountDownTimer isAddedTimer = new CountDownTimer(60000, 2000) {
        @Override
        public void onTick(long l) {
            if (!isAdded) {
                getOtherAppList();
            } else {
                isAdded = true;
                isAddedTimer.cancel();
            }
        }

        @Override
        public void onFinish() {
        }
    };
    private CountDownTimer isReceiveRecordTimer = new CountDownTimer(60000, 2000) {
        @Override
        public void onTick(long l) {
            if (!isReceiveRecord)
                getRecord();
            else {
                isReceiveRecord = true;
                isReceiveRecordTimer.cancel();
            }
        }

        @Override
        public void onFinish() {

        }
    };
    private CountDownTimer studyTimer           = new CountDownTimer(120000,1000) {
        @Override
        public void onTick(long l) {
            long secondsInFuture = millisTime/1000;
            int seconds = (int) (secondsInFuture%60);
            int minutes = (int) (secondsInFuture/60);
            txvSaverTime.setText(String.format("%2d:%2d",minutes,seconds));
            millisTime -= 1000;
            wldProgress.setProgressValue(mTimerMarker++);
        }

        @Override
        public void onFinish() {

        }
    };
    private int pickMinute, pickHour;

    public static void start(Context context, int hour, int minute) {
        Intent intent = new Intent(context, ScreenSaverActivity.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ScreenSaverActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_saver_pause, R.id.btn_saver_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_saver_pause:
                break;
            case R.id.btn_saver_end:
                break;
        }
    }

    @Override
    protected void onPause() {
        RogueUtils.startRoit(this,App.pylist);
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        ButterKnife.bind(this);

        pickHour   = getIntent().getIntExtra("hour",0);
        pickMinute = getIntent().getIntExtra("minute",0);
        transAppList();
        mScreenWatcher.register(mScreenListener);
        mScreenWatcher.register(mScreenListener);
    }

    //发起者和接受者互相传输Applist
    private void transAppList(){
        List<AppInfos> appList  = AppInfoUtils.getAppInfos();
        List<String> appLabelList  = new ArrayList<>();
        for (int i = 0; i <appList.size() ; i++) {
            appLabelList.add(appList.get(i).getLabel());
        }
        RxFactory.getRetrofitService()
                .postTranslist(new AppPick(App.otherUserId,0,appLabelList))
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
                        isAddedTimer.start();
                    }
                });
    }
    //发起者和接受者互相传输使修改过的list
    private void transModifiedList(){
        String userMarker = PreferenceUtils.getString(R.string.user_marker);
        RxFactory.getRetrofitService()
                .postTranslist(new AppPick(userMarker,1,otherBlackList))
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
                        isModifyTimer.start();
                    }
                });
    }

    //发起者和接受者互相查询是否有这个选择禁用的list
    //一旦获取到,开始执行倒计时
    private void getOtherAppList() {
        //获得对方的
        RxFactory.getRetrofitService()
                .postGetModifiedAppList(new UserId(App.otherUserId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppPicker>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(AppPicker appPick) {
                        isAddedTimer.cancel();
                        String apps = appPick.getApps();
                        otherUserList = parseString(apps);
                        mBtnEnter.setVisibility(View.VISIBLE);
                        initListView();
                    }
                });
    }

    //获取对方修改过得AppList
    private void getFinalAppList() {
        String userMarker = PreferenceUtils.getString(R.string.user_marker);
        RxFactory.getRetrofitService()
                .postGetModifiedAppList(new UserId(userMarker))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppPicker>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        int code = NetWorkUtils.getresponseCode(e);
                        if (code == 404)
                            return;
                    }

                    @Override
                    public void onNext(AppPicker appPicker) {
                        isFinal = true;
                        isModifyTimer.cancel();
                   //     initRecyclerView(appPicker.getApps());
                 //       mRlayoutApps.setVisibility(View.VISIBLE);
                        //等待对方确定了之后 才开始轮训
                        long millis = (pickHour * 3600 + pickMinute * 60) * 1000;
                        wldProgress.setAnimDuration(millis);
                        //倒计时
                        //studyTimer = new MyTimer(millis, 1000);
                   //     studyTimer.setTimerListener(timerListener);
                     //   studyTimer.onTick(1000);
                       // studyTimer.onFinish();
                        mHomeWatcher.startWatch();
                        mHomeWatcher.setOnHomePressedListener(homePressedListener);
                    }
                });
    }

    //在完成倒计时之后互相发送消息
    private void getRecord() {
        RxFactory.getRetrofitService()
                .postGetUserRecord(new UserId(App.otherUserId))
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserRecord>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserRecord userRecord) {
                        isReceiveRecordTimer.start();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
                AlertDialogUtils.show(ScreenSaverActivity.this, btnSaverEnd);
                return false;
        }
        if(keyCode==KeyEvent.KEYCODE_MENU){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setClassName(App.sAppPackagename,App.sAppPackagename+"."+"ScreenSaverActivity");
            App.getContext().startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initRecyclerView(List<String> blackList) {
        btnFinalEnter.bringToFront();
        btnFinalEnter.setVisibility(View.VISIBLE);
        List<AppInfos> appInfoList = AppInfoUtils.getAppInfos();
        List<String> appLabelList = new ArrayList<>();
        List<Drawable> appDrawableList = new ArrayList<>();
        for (int i = 0; i < appInfoList.size(); i++) {
            appLabelList.add(appInfoList.get(i).getLabel());
            appDrawableList.add(appInfoList.get(i).getIcon());
        }
        List<AppInfos> whiteList = new ArrayList<>();
        for (int i = 0; i < blackList.size(); i++) {
            if (!appLabelList.contains(blackList.get(i))) {
                whiteList.add(new AppInfos(appDrawableList.get(i), appLabelList.get(i)));
            }
        }
        for(int i=blackList.size()-1;i<appDrawableList.size();i++){
            whiteList.add(new AppInfos(appDrawableList.get(i), appLabelList.get(i)));
        }
        GridLayoutManager manager = new GridLayoutManager(this,3);
        mAppsAdapter = new AppsAdapter(this,whiteList);
        mRecyclerView.setAdapter(mAppsAdapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setVisibility(View.VISIBLE);
        mBgView.setVisibility(View.VISIBLE);
        mBgView2.setVisibility(View.VISIBLE);
    }

    private void initListView() {
        mAppotherAdapter = new AppsOthersAdapter(this, otherUserList);
        List<String> blackList = mAppotherAdapter.getBlackList();
        otherBlackList = blackList;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.bringToFront();
        mBgView.setVisibility(View.VISIBLE);
        mBgView2.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAppotherAdapter);
        mBtnEnter.setVisibility(View.VISIBLE);
        mBtnEnter.bringToFront();
    }
    //通过百分比设置
    private long setWaveLoadingAnimation(int pickHour,int pickMinute){
        long millis = pickMinute*60 + pickHour*3600;
        CountDownTimer countDownTimer = new CountDownTimer(millis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (mTimerMarker++/millis);
                wldProgress.setProgressValue(progress);
            }
            @Override
            public void onFinish() {
            }
        };
    }

}
