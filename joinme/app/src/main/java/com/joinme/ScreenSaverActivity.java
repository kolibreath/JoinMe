package com.joinme;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joinme.adapter.AppsAdapter;
import com.joinme.appChoosingView.BannedAppChoosingDialog;
import com.joinme.miscellaneous.App;
import com.joinme.model.AppPick;
import com.joinme.model.AppPicker;
import com.joinme.model.Status;
import com.joinme.model.UserId;
import com.joinme.model.UserRecord;
import com.joinme.services.RxFactory;
import com.joinme.utils.AlertDialogUtils;
import com.joinme.utils.RogueUtils;
import com.joinme.watchers.HomeWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.waveloadingview.WaveLoadingView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.keycode;

/**
 * Recreated by kolibreth on 2018/4/20.
 */

public class ScreenSaverActivity extends AppCompatActivity {
    @BindView(R.id.txv_saver_time)
    TextView txvSaverTime;
    @BindView(R.id.btn_saver_end)
    Button btnSaverEnd;
    @BindView(R.id.wld_view)
    WaveLoadingView wldProgress;
    @BindView(R.id.btn_saver_pause)
    Button mBtnSaverPause;

//    private ScreenWatcher mScreenWatcher;

    private List<String> mBannedApps ;
    private static int sInterval = 1000;
    private List<String> otherUserList = new ArrayList<>();
    private List<String> otherBlackList = new ArrayList<>();

    private RxCountDownTimer mCountTimer = new RxCountDownTimer();
    private RxTask mIsReceiveRecordTask = new RxTask(sInterval,
            new Subscriber() {
                @Override
                public void onCompleted() { }
                @Override
                public void onError(Throwable e) { }
                @Override
                public void onNext(Object o) {getRecord();}});

    //从oncreate开始就开始记录时间 如果从拿到双方的applist ->　修改完成的时间控制在３０ｓ内
    private RxCountDownTimer mTotalCountdownTimer = new RxCountDownTimer();
    private int pickMinute = 7, pickHour = 0;

    public static void start(Context context, int hour, int minute) {
        Intent intent = new Intent(context, ScreenSaverActivity.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        context.startActivity(intent);
    }


    @OnClick({R.id.btn_saver_pause, R.id.btn_saver_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_saver_pause:
                transModifiedList();
                break;
            case R.id.btn_saver_end:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver);
        ButterKnife.bind(this);
//
//        pickHour   = getIntent().getIntExtra("hour",0);
//        pickMinute = getIntent().getIntExtra("minute",0);

//        registerScreeWatcher();
        registerHomeWatcher();

        RogueUtils.startRiot(App.mockList);

        //开始总倒计时 倒计时完成之后的回调
        //todo 查证一下这个applist 获取有没有问题
        //因为这个写法不太好 就本地mock一下被ban掉的apps
        mTotalCountdownTimer.onFinish(18)
                .flatMap(o -> getAppList(App.userId))
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    //获取别人修改过得自己的ｉｄ
                    public void onNext(Object o) {
                        long millis = (pickHour * 3600 + pickMinute * 60) * 1000;
                        wldProgress.setAnimDuration(millis);

                        //todo 显示倒计时的时间
                        mCountTimer.countdown(Integer.valueOf(pickHour*3600 + pickMinute*60))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber() {
                                    @Override
                                    public void onCompleted() { }
                                    @Override
                                    public void onError(Throwable e) {e.printStackTrace();}
                                    @Override
                                    public void onNext(Object o) {
                                        Integer integer = (Integer)o;
                                        txvSaverTime.setText(String.valueOf(integer)+"s");
                                    }
                                });
                        //todo start to rogue!
                        String json = ((AppPicker)o).getApps();
//                        mBannedApps = parseString(json);


                    }});

        //获取别人的list
        getAppList(App.otherUserId)
                        .subscribe(new Subscriber<AppPicker>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @Override
                    public void onNext(AppPicker appPick) {
                        //获取成功
                        String apps = appPick.getApps();
                        otherUserList = parseString(apps);
                        initListView();}});

        RxBus.getDefault().toObservable(TransModifyEvent.class)
                .subscribe(new Subscriber<TransModifyEvent>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @Override
                    public void onNext(TransModifyEvent transModifyEvent) {
                        Log.d("fuckx", "onNext: ");
                        transModifiedList();}
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    private void registerHomeWatcher(){
        HomeWatcher mHomeWatcher = new
                HomeWatcher(ScreenSaverActivity.this);
        mHomeWatcher.startWatch();
        mHomeWatcher.setOnHomePressedListener
                (new HomeWatcher.OnHomePressedListener() {
                    @Override
                    public void onHomePressed() {
                        App.UNLOCKTIMES++;}
                    @Override
                    public void onHomeLongPressed() { }});
    }

    private void registerScreeWatcher(){
//        mScreenWatcher = new ScreenWatcher(ScreenSaverActivity.this);
//        ScreenWatcher.ScreenListener listener =
//                () -> RogueUtils.excuteScreenLocker("ScreenSaverActivity");
//        mScreenWatcher.register(listener);

    }

    //发起者和接受者互相传输使修改过的list
    private void  transModifiedList(){
        Log.d("fuck", "transModifiedList: ");
        RxFactory.getRetrofitService()
                .postTranslist(new AppPick(App.otherUserId,
                        1,otherBlackList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();}
                    @Override
                    public void onNext(Status status) { }
                });
    }

    //发起者和接受者互相查询是否有这个选择禁用的list
    //一旦获取到,开始执行倒计时
    //参数　如果是自己的ｉｄ　就是需要获取自己的applist 反之亦然
    private Observable getAppList(String userId) {
        //获得对方的
        return RxFactory.getRetrofitService()
                .postGetModifiedAppList(new UserId(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //在完成倒计时之后互相发送消息
    private void getRecord() {
        RxFactory.getRetrofitService()
                .postGetUserRecord(new UserId(App.otherUserId))
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserRecord>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace(); }
                    @Override
                    public void onNext(UserRecord userRecord) { mIsReceiveRecordTask.start(); }
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


    private void initListView() {
        AppsAdapter adapter = new AppsAdapter(otherUserList);
        otherBlackList = adapter.getBlackList();

        BannedAppChoosingDialog dialog = BannedAppChoosingDialog.newInstance(otherUserList,adapter);
        dialog.show(getSupportFragmentManager(),"app_chooing_dialog");

    }

    @Override
    protected void onPause() {
        super.onPause();
//        mScreenWatcher.unregister();ing
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
