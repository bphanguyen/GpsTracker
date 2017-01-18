package com.application.bach.gpstracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceLock extends Service {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";


    public ServiceLock() {
    }


    @Override
    public void onCreate() {
        //Replace LENGTH_LONG by LENGTH_SHORT after test
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        myLog.i(TAG, "Intent received : ServiceLock started");

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Si le système ferme le service, alors le paramétre START_STICKY permet un redémarrage automatique
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
