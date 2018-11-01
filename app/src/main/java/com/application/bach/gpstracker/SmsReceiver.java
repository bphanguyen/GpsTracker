package com.application.bach.gpstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by bach on 08/01/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";

    private static final String LOCK_ACTION = "lockMyPhone";

    private static final String STOP_ACTION = "stopService";

    @Override
    public void onReceive(Context context, Intent intent) {

        myLog.i(TAG, "Intent received :"+intent.getAction());

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals( intent.getAction() )){

            Bundle bundle = intent.getExtras();

            String info = intent.getStringExtra("format");

            if (bundle != null){

                Object[] pdus = (Object[]) bundle.get("pdus");

                int pdusLen = pdus.length;

                final SmsMessage[] messages = new SmsMessage[pdusLen];

                for (int i = 0; i < pdusLen; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], info);
                }

                if (messages.length > -1){

                    myLog.i(TAG, "Message receiver : "+messages[0].getMessageBody());

                    Toast.makeText(context, messages[0].getMessageBody(), Toast.LENGTH_LONG).show();

                    checkSms(messages[0].getMessageBody(), context);
                }

            }

        }

    }

    /*--package--*/void checkSms(String SMS, Context context){

        //Stoppe la réception des Sms : ! Attention le système n'est plus pilotable
        /*
        if (SMS.equals("STOP")){
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }
        */

        if (STOP_ACTION.equals(SMS)){

            broadCastMessage(context, STOP_ACTION);

        }
    }


    private void broadCastMessage(Context context, String msg){

        Log.d(TAG, "Broadcasting message");

        Intent intent = new Intent(LOCK_ACTION);

        intent.putExtra("message", msg);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
}
