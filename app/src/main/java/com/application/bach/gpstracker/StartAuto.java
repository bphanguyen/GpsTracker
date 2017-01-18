package com.application.bach.gpstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAuto extends BroadcastReceiver {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";

    public StartAuto() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        myLog.i(TAG, "BroadcastReceiver StartAuto on BOOT_COMPLETED");

        Intent myIntent = new Intent(context, ServiceLock.class);
        context.startService(myIntent);
    }
}
