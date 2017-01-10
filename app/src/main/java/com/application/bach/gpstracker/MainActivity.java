package com.application.bach.gpstracker;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends RuntimePermissionsActivity {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";

    private static final int REQUEST_PERMISSIONS = 20;

    private AppCompatActivity activity = this;

    private String myText = null;

    private Context myAppContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //askUserPermission(this, Manifest.permission.SEND_SMS);

        //myAppContext = this.getApplicationContext();

        //Phone number
        //final EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        //myText = editTextPhone.getText().toString();

        //Send button
        Button buttonTest = (Button) findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                askUserPermission(activity, Manifest.permission.SEND_SMS);

                //ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS},
                //        REQUEST_PERMISSIONS);

            }
        });


        //Récepteur de Sms
        SmsReceiver myReceiver = new SmsReceiver();

        this.registerReceiver(myReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        myLog.i(TAG, "New Message");
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        prepareSendingSms();

    }

    private void prepareSendingSms(){
        myAppContext = this.getApplicationContext();

        //Phone number
        final EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        myText = editTextPhone.getText().toString();

        final EditText editSmallSmg = (EditText) findViewById(R.id.editSmallMsg);
        String mySmallSmg = editSmallSmg.getText().toString();

        if ("".equals(mySmallSmg)){
            mySmallSmg = "test SMS";
        }

        myLog.d(TAG, "myText : "+myText);
        myLog.d(TAG, "mySmallSmg : "+mySmallSmg);
        myLog.d(TAG, "myAppContext : "+myAppContext);



        Sms sms = new Sms();
        sms.sendSMS(myText, mySmallSmg, myAppContext);
    }


    /**
     * Ask User for Permissions if necessary
     * @param activity
     * @param permission
     */
    private void askUserPermission(AppCompatActivity activity, String permission){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS},
                        REQUEST_PERMISSIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                //myMethod();
            }
        }else{
            prepareSendingSms();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
