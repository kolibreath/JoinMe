package com.joinme.appChoosingView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.joinme.R;
import com.joinme.RxBus;
import com.joinme.TransModifyEvent;
import com.joinme.adapter.AppsAdapter;

import java.util.List;

/**
 * Created by kolibreath on 17-11-4.
 */

public class BannedAppChoosingDialog extends CenterDialogFragment{
//    @BindView(R.id.btn_app_choosing_cancel)
    Button mBtnCancel;
//    @BindView(R.id.btn_app_choosing_enter)
    Button mBtnEnter;
//    @BindView(R.id.rv_app_choosing_list)
    RecyclerView mRvAppList;


    private List<String> mAppList;
    private Dialog mDialog;

    private static AppsAdapter mAppAdapter;
    private static List<String> mUserList;

//    private DialogClickListener mCancelListener, mEnterListener


    public static BannedAppChoosingDialog newInstance(List<String> userApplist,
                                                      AppsAdapter adapter){
        mUserList = userApplist;
        mAppAdapter  = adapter;
        return new BannedAppChoosingDialog();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_list,null);
        mBtnEnter  = view.findViewById(R.id.btn_app_choosing_enter);
        Log.d("fuck", "onCreateDialog: "+mBtnEnter);
        mBtnCancel = view.findViewById(R.id.btn_app_choosing_cancel);
        mRvAppList = view.findViewById(R.id.rv_app_choosing_list);

        mBtnEnter.setOnClickListener(view1->{
            Log.d("fuck", "onCreateDialog: "+"onclicked");
            dismiss();
            RxBus.getDefault().send(TransModifyEvent.class);
        });
        mBtnCancel.setOnClickListener(view2->{dismiss();});

        mDialog = createCenterDialog(view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAppList.setLayoutManager(manager);
        mRvAppList.setAdapter(mAppAdapter);
        return mDialog;
    }

    public interface DialogClickListener {

        void onclick();
    }
}
