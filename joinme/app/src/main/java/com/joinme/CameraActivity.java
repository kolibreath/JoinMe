package com.joinme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.joinme.faceRecon.FaceView;
import com.joinme.utils.SnackBarUtils;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private int sRequestCode = 1;
    private FaceView mFaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mFaceView = findViewById(R.id.faceview);
        activateCamera();
    }

    private void activateCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            Uri imageUri = Uri.fromFile(getTempFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(intent, sRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == sRequestCode && resultCode == RESULT_OK) {
            Bitmap bitamp = getScaleBitmap(this,getTempFile().getPath());
            if(bitamp !=null){
                mFaceView.setBitmap(bitamp);
            }

            if(mFaceView.isEyesOpen()){
                Log.d("fuck", "onActivityResult:zaoshang ");
                SnackBarUtils.showShort(mFaceView,"早上好~");
//                finish();
            }else{
                SnackBarUtils.showShort(mFaceView,"起床了!!");
                Log.d("fuck", "onActivityResult:fafafafa");
            }
        }
    }

    private File getTempFile() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tempFile;
        }
        return null;
    }

    private Bitmap getScaleBitmap(Context ctx,String filePath){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > screenWidth)
                opt.inSampleSize = bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = bmpHeght / screenHeight;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        return bmp;
    }
}
