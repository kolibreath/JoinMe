package com.joinme.utils;
/**
 * Created by kolibreath on 17-10-21.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

import com.joinme.miscellaneous.App;

/**
 *使用说明：
 * 1.在oncreate中调用构造器初始化 onResume 中register 在onPause中解除绑定
 * 2.
 */


public class SensorUtils implements SensorEventListener {

    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 100;
    // 加速度变化阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_THRESHOLD = 1200;

    private Context mContext;
    private SensorManager mSensorManager = null;
    private Vibrator mVibrator = null;
    private boolean isFirsttime  = true;
    public SensorUtils() {
        mContext = App.getContext();
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;

        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        Log.d("keeping", "onSensorChanged: ");

        lastUpdateTime = currentUpdateTime;
        float[] values = event.values;

        // 获得x,y,z加速度
        float x = values[0];
        float y = values[1];
        float z = values[2];

        // 获得x,y,z加速度的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        if (speed > SPEED_THRESHOLD) {
            mVibrator.vibrate(700);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerSensor() {
        Sensor sensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (null != sensor)
            mSensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegisterSensor() {

        lastX = 0;
        lastY = 0;
        lastZ = 0;
        mSensorManager.unregisterListener(this);
    }

}
