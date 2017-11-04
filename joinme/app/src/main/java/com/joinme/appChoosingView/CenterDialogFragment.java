package com.joinme.appChoosingView;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.joinme.R;

/**
 * Created by kolibreath on 17-11-4.
 */

public class CenterDialogFragment extends DialogFragment{

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Dialog createCenterDialog(View view){
        Dialog dialog = new Dialog(getContext(), R.style.CenterDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.drawable.bg_bottom_dialog);
        return dialog;
    }
}
