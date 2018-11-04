package com.application.bach.gpstracker;

import android.Manifest;
import android.app.ActivityManager;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends RuntimePermissionsActivity {

    public Log myLog;

    private static final String TAG = "GpsTrackerActivity";

    private static final int REQUEST_PERMISSIONS = 20;

    private AppCompatActivity activity = this;

    private String myText = null;

    private Context myAppContext = null;

    EditText editTextPhone;
    EditText editTextPass;
    CheckBox checkBoxRecord;

    //Context myContext;
    Controller myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //askUserPermission(this, Manifest.permission.SEND_SMS);

        myAppContext = this.getApplicationContext();

        myController = new Controller(myAppContext, MainActivity.this);

        //Phone number
        //final EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        //myText = editTextPhone.getText().toString();


        //Récepteur de Sms
        SmsReceiver myReceiver = new SmsReceiver();

        this.registerReceiver(myReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        myLog.i(TAG, "New Message");

        controlButtons();



    }


    private void controlButtons(){

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


        editTextPhone = (EditText)  findViewById(R.id.editTextPhone);
        editTextPass = (EditText) findViewById(R.id.editTextPassword);
        checkBoxRecord = (CheckBox) findViewById(R.id.checkboxRecord);


        //Unlock
        Button buttonUnlock = (Button) findViewById(R.id.buttonUnlock);
        buttonUnlock.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            if ( (myController.unlock(editTextPass.getText().toString())) ){
                editTextPhone.setText(myController.getTel());
                checkBoxRecord.setChecked(myController.getRecord());
            }else{
                Toast.makeText(myAppContext, "Unlock failed", Toast.LENGTH_LONG).show();
            }

            }

        });


        //Start
        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            if (myController.isStartOk(editTextPhone.getText().toString(), editTextPass.getText().toString())){

                if (checkBoxRecord.isChecked()){
                    ManageSettings.isRecord = true;
                }else{
                    ManageSettings.isRecord = false;
                }

                Toast.makeText(myAppContext, "Start service", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(myAppContext, "Can't start service", Toast.LENGTH_LONG).show();
            }

            }

        });


        //Stop
        Button buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            if (myController.stopIsOk(editTextPass.getText().toString())){

                if (myController.broadCastMessage("stopService")){
                    Toast.makeText(myAppContext, "Stop service", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(myAppContext, "Can't stop service", Toast.LENGTH_LONG).show();
            }

            }

        });


        //Checkbox
        checkBoxRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

            if (myController.unlock(editTextPass.getText().toString())){
                if (checkBoxRecord.isChecked()){
                    ManageSettings.isRecord = true;
                }else{
                    ManageSettings.isRecord = false;
                }
            }

            }
        });

    }


    @Override
    public void onPause(){

        myController.onPauseOk(editTextPhone.getText().toString(), editTextPass.getText().toString());

        super.onPause();
    }



    public boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }

        return false;

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
