package com.example.juliod07.backgroundtest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;

/**
 * Created by JulioD07 on 11/16/2018.
 */

public class BackgroundService extends Service {

    private SensorManager sensorManager;
    private float acelVal;  //CURRENT ACCELERATOR VALUE AND GRAVITY
    private float acelLast; //LAST ACCELERATOR VALUE AND GRAVITY
    private float shake; //ACCELERATOR VALUE DIFFER FROM GRAVITY

    private ImageButton imageButton;
    private Context context = this;

    boolean ison = false;
    private CameraManager mCameraManager;
    private String mCameraId;
    private MainActivity mainActivity;


    class MyServiceThread implements Runnable {
        int service_id;


        MyServiceThread(int service_id) {
            this.service_id = service_id;
        }

        @Override
        public void run() {

            ShakeDetector.create(context, new ShakeDetector.OnShakeListener() {
                @Override
                public void OnShake() {
                    tocarBoton();
                    Log.d("PRUEBAS: ", "AQUI CORRE EL TOAST CON BACKGROUND SERVICE");


                }
            });

        }


    }

    private void tocarBoton() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (!ison) {

            ison = true;
            turnOnFlashLight();
            imageButton.setImageResource(R.drawable.power);
            stopService(new Intent(this, BackgroundService.class));

        } else {

            ison = false;
            turnOffFlashLight();
            imageButton.setImageResource(R.drawable.off);
            startService(new Intent(this, BackgroundService.class));


        }
    }

    private void turnOffFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                imageButton.setImageResource(R.drawable.off);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void turnOnFlashLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //AQUI VA LA ACCION A EJECUTAR
        Toast toast = Toast.makeText(getApplicationContext(), "DO NOT SHAKE ME onstartcommand", Toast.LENGTH_LONG);
        toast.show();

        Thread thread = new Thread(new MyServiceThread(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        //AQUI TERMINAR LA ACCION
    }
}
