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


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p/>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p/>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p/>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p/>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
