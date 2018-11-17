package com.example.juliod07.backgroundtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JulioD07 on 11/16/2018.
 */

public class BackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //AQUI VA LA ACCION A EJECUTAR
        Toast toast =Toast.makeText(getApplicationContext(), "DO NOT SHAKE ME", Toast.LENGTH_LONG);
        toast.show();
        Log.d("PRUEBAS: ", "AQUI CORRE EL TOAST CON BACKGROUND SERVICE");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity mainActivity = new MainActivity();
        mainActivity.turnOffFlashLight();

        //AQUI TERMINAR LA ACCION
    }
}
