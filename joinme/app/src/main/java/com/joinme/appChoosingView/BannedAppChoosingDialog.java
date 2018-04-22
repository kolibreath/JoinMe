package com.joinme.appChoosingView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.joinme.R;
import com.joinme.model.AppInfos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kolibreath on 17-11-4.
 */

public class BannedAppChoosingDialog extends CenterDialogFragment{
    @BindView(R.id.btn_app_choosing_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_app_choosing_enter)
    Button mBtnEnter;
    @BindView(R.id.rv_app_choosing_list)
    RecyclerView mRvAppList;

    private Dialog mDialog;
    private List<AppInfos> appInfosList;
    private DialogClickListener mEnterListener;
    private DialogClickListener mCancelListener;

    public void setCancelListener(DialogClickListener cancelListener){
        if(cancelListener!=null){
            this.mCancelListener  = cancelListener;
            mBtnCancel.setOnClickListener(view12 -> {
                mCancelListener.onclick();
                mDialog.dismiss();
            });
        }
    }

    public void setEnterListener(DialogClickListener enterListener){
        if(enterListener!=null){
            this.mEnterListener = enterListener;
            mBtnEnter.setOnClickListener(view12 -> {
                mEnterListener.onclick();
                mDialog.dismiss();
            });
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_recycler_list,null);
        ButterKnife.bind(this,view);
         mDialog = createCenterDialog(view);

        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        mRvAppList.setLayoutManager(manager);
        return mDialog;
    }

    public void setAppInfosList(List<AppInfos> list){
        this.appInfosList = list;
    }

    public interface DialogClickListener {

        void onclick();}
}
