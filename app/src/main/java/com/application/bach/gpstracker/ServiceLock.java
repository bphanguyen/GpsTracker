package com.application.bach.gpstracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class ServiceLock extends Service {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";

    private static final String LOCK_ACTION = "lockMyPhone";

    private static final String STOP_ACTION = "stopService";

    //SMS
    SmsReceiver myReceiver;
    BroadcastReceiver msgCom;


    public ServiceLock() {
    }


    @Override
    public void onCreate() {

        initSms();

        initBroadcast();

        //Replace LENGTH_LONG by LENGTH_SHORT after test
        Toast.makeText(this, "ServiceLock.onCreate() Service started", Toast.LENGTH_SHORT).show();

        myLog.i(TAG, "Intent received : ServiceLock started");

        super.onCreate();
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "ServiceLock.onDestroy() Service destroyed", Toast.LENGTH_SHORT).show();

        LocalBroadcastManager.getInstance(this.getApplicationContext())
                .unregisterReceiver(myReceiver);

        unregisterReceiver(myReceiver);

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


    private void initBroadcast(){

        msgCom = new MsgCom();

        //Récepteur local pour les intents
        LocalBroadcastManager.getInstance(this).registerReceiver(msgCom,
                new IntentFilter(LOCK_ACTION));
    }


    private void initSms(){
        //Réception des SMS
        myReceiver = new SmsReceiver();

        this.registerReceiver(myReceiver,
                new IntentFilter("android.provide.Telephony.SMS_RECEIVED"));
    }


    private class MsgCom extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){

            if (TextUtils.equals(intent.getAction(), LOCK_ACTION)){

                String message = intent.getStringExtra("message");

                Log.d("MsgCom receiver", "Got message : "+message);

                if (STOP_ACTION.equals(message)){

                    ServiceLock.this.stopSelf();

                    Log.d("MsgCom receiver", "Service stopped.");
                }

            }

        }
    }

}
