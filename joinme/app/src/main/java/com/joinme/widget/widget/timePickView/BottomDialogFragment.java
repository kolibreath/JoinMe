package com.joinme.widget.widget.timePickView;

//import android.app.Dialog;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.joinme.R;

/**
 * Created by kolibreath on 17-10-21.
 */

public class BottomDialogFragment extends DialogFragment{

    public BottomDialogFragment newInstance(){
        BottomDialogFragment fragment = new BottomDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public Dialog createBottomDialog(View view){
            Dialog dialog = new Dialog(getContext(), R.style.BottomDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.drawable.bg_bottom_dialog);
        return dialog;
    }
}
