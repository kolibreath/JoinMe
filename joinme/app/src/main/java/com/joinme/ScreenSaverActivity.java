package com.joinme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.joinme.adapter.AppsAdapter;
import com.joinme.appChoosingView.BannedAppChoosingDialog;
import com.joinme.miscellaneous.App;
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

import static android.R.attr.keycode;

/**
 * Recreated by kolibreth on 2018/4/20.
 */

public class ScreenSaverActivity extends AppCompatActivity {
    @BindView(R.id.txv_saver_time)
    RxCDTextView txvSaverTime;
    @BindView(R.id.btn_saver_end)
    Button btnSaverEnd;
    @BindView(R.id.wld_view)
    WaveLoadingView wldProgress;


    private static int sInterval = 1000;
    private List<String> otherUserList = new ArrayList<>();
    private List<String> otherBlackList = new ArrayList<>();

    private RxTask mIsAddedTask = new RxTask(sInterval, o -> getOtherAppList());
    private RxTask mIsReceiveRecordTask = new RxTask(sInterval, o->getRecord());
    private RxTask mIsModifyTask = new RxTask(sInterval,o->getFinalAppList());

    //设置一个倒计时
    private RxCountDownTimer mCountdownTimer = new RxCountDownTimer();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        ButterKnife.bind(this);

        pickHour   = getIntent().getIntExtra("hour",0);
        pickMinute = getIntent().getIntExtra("minute",0);
        transAppList();

        registerScreeWatcher();
    }

    private void registerScreeWatcher(){
        ScreenWatcher mScreenWatcher = new ScreenWatcher(ScreenSaverActivity.this);
        ScreenWatcher.ScreenListener listener = () -> RogueUtils.excuteScreenLocker("ScreenSaverActivity");
        mScreenWatcher.register(listener);

    }
    //发起者和接受者互相传输Applist
    private void transAppList(){
        List<String> appLabelList  = AppInfoUtils.getAppNames(AppInfoUtils.getAppInfos());

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
                       mIsAddedTask.start();
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
                        //60 秒倒计时
                        mCountdownTimer.countdown(60);
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
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @Override
                    public void onNext(AppPicker appPick) {
                        //获取成功
                        mIsAddedTask.stop();
                        String apps = appPick.getApps();
                        otherUserList = parseString(apps);
                        initListView();}});
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

                        mIsModifyTask.start();
                        //     initRecyclerView(appPicker.getApps());
                        //  mRlayoutApps.setVisibility(View.VISIBLE);
                        //等待对方确定了之后 才开始轮训
                        long millis = (pickHour * 3600 + pickMinute * 60) * 1000;
                        wldProgress.setAnimDuration(millis);
                        //倒计时
//                        studyTimer = new MyTimer(millis, 1000);
//                        studyTimer.setTimerListener(timerListener);
//                        studyTimer.onTick(1000);
//                        studyTimer.onFinish();
                        //todo
                        txvSaverTime.startCountdown((int) (millis/100));


                        HomeWatcher mHomeWatcher = new HomeWatcher(ScreenSaverActivity.this);
                        mHomeWatcher.startWatch();
                        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
                            @Override
                            public void onHomePressed() {
                                App.UNLOCKTIMES++;
                            }

                            @Override
                            public void onHomeLongPressed() {

                            }
                        });
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
//                        isReceiveRecordTimer.start();
                        mIsReceiveRecordTask.start();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keycode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialogUtils.show(ScreenSaverActivity.this, btnSaverEnd);
                break;
            case KeyEvent.KEYCODE_MENU:
                RogueUtils.excuteScreenLocker("ScreenSaverActivity");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    //没有写点击回调
    private void initListView() {
        AppsAdapter adapter = new AppsAdapter(otherUserList);

        List<String> blackList = adapter.getBlackList();
        otherBlackList = blackList;
        BannedAppChoosingDialog dialog = new BannedAppChoosingDialog();
        dialog.show(getSupportFragmentManager(),"app_chooing_dialog");
    }
    private List<String> parseString(String json){
        String apps[] = json.split(",");
        List<String> applist = new ArrayList<>();
        for(int i=0;i<apps.length;i++){
            if(i==0){
                String temp = apps[i].substring(2,apps[i].length()-2);
                applist.add(temp);
            }
            if(i==apps.length-1){
                String temp = apps[i].substring(1,apps[i].length()-3);
                applist.add(temp);
            }
            String temp = apps[i].substring(2,apps[i].length()-1);
            applist.add(temp);
        }
        return applist;
    }
}
