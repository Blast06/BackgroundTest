package com.example.juliod07.backgroundtest;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SensorManager sensorManager;
    private float acelVal;  //CURRENT ACCELERATOR VALUE AND GRAVITY
    private float acelLast; //LAST ACCELERATOR VALUE AND GRAVITY
    private float shake; //ACCELERATOR VALUE DIFFER FROM GRAVITY

    private ImageButton imageButton;

    boolean ison = false;
    private CameraManager mCameraManager;
    private String mCameraId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.off);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;


    }


    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));

            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if (shake > 17) {
                //DO WHAT YOU WANT, .... IDK WHAT TO USE...XD
                Toast toast = Toast.makeText(getApplicationContext(), "DO NOT SHAKE ME", Toast.LENGTH_LONG);
                toast.show();
                Log.d("PRUEBAS: ", "AQUI CORRE EL TOAST");
                tocarBoton();

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    public void onClick(View view) {


    }


    public void encender(View view) {
        tocarBoton();


    }

    public void tocarBoton() {
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

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                imageButton.setImageResource(R.drawable.off);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //esto es para usar con la libreria del ShakeDetector...
//    @Override
//    public void OnShake() {
//
//    }
}


