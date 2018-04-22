package com.joinme.faceRecon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by kolibreath on 17-10-21.
 */

//只有对着镜头拍出睁开眼睛才可以
public class FaceView extends View {
    private Bitmap mBitmap;
    private SparseArray mFaces;
    public FaceView(Context context) {
        super(context);
    }
    public FaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public FaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
        refresh();
    }
    public void refresh(){
        FaceDetector detector = new FaceDetector.Builder( getContext() )
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }
         invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceBox(canvas, scale);
        }
    }
    private double drawBitmap(Canvas canvas){
            double viewWidth = canvas.getWidth();
            double viewHeight = canvas.getHeight();
            double imageWidth = mBitmap.getWidth();
            double imageHeight = mBitmap.getHeight();
            double scale = Math.min( viewWidth / imageWidth, viewHeight / imageHeight );

            Rect destBounds = new Rect( 0, 0, (int) ( imageWidth * scale ), (int) ( imageHeight * scale ) );
            canvas.drawBitmap( mBitmap, null, destBounds, null );
            return scale;
    }
    private void drawFaceBox(Canvas canvas, double scale) {
        //paint should be defined as a member variable rather than
        //being created on each onDraw request, but left here for
        //emphasis.
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        for( int i = 0; i < mFaces.size(); i++ ) {
            Face face = (Face) mFaces.valueAt(i);

            left = (float) ( face.getPosition().x * scale );
            top = (float) ( face.getPosition().y * scale );
            right = (float) scale * ( face.getPosition().x + face.getWidth() );
            bottom = (float) scale * ( face.getPosition().y + face.getHeight() );

            canvas.drawRect( left, top, right, bottom, paint );
        }
    }
    public boolean isEyesOpen(){
        float pLeftEyeOpen,pRightEyeOpen;
        for (int i = 0; i <mFaces.size() ; i++) {
            if (mFaces.size() == 1) {
                Face face = (Face) mFaces.valueAt(i);
                pLeftEyeOpen = face.getIsLeftEyeOpenProbability();
                pRightEyeOpen = face.getIsRightEyeOpenProbability();
                if(pLeftEyeOpen>0.8&&pRightEyeOpen>0.8){
                    return true;
                }
            }
        }
        return false;
    }
}
