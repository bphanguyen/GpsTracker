package com.application.bach.gpstracker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by bach on 01/11/2018.
 */

public class Controller {

    Context myContext;
    String pass;
    String tel;
    Boolean modeInit = false;
    Boolean unlock = false;
    ManageSettings manageData;
    MainActivity myActivity;

    Controller(Context myContext, MainActivity myActivity){
        this.myContext = myContext;
        this.myActivity = myActivity;

        manageData = new ManageSettings();

        //Chargement des paramétres
        loadSettings();
    }

    public Boolean getRecord(){
        return ManageSettings.isRecord;
    }

    public String getTel(){
        return tel;
    }

    public Boolean unlock(String pass){

        //Chargement des paramétres
        ManageSettings loadMyData = new ManageSettings();
        HashMap<String, String> data = new HashMap<String, String>();
        loadMyData.restoreData(myContext, data);

        Log.d("Controller", "unlock modeInit = "+modeInit);
        Log.d("Controller", "unlock unlock = "+unlock);
        Log.d("Controller", "unlock pass = "+pass);
        Log.d("Controller", "unlock pass encoded = "+loadMyData.encode(pass));
        Log.d("Controller", "unlock data.get(PASS) = "+data.get("PASS"));
        Log.d("Controller", "unlock tel = "+tel);
        Log.d("Controller", "unlock ManageSettings.isRecord = "+ManageSettings.isRecord);

        if (pass == null || pass.trim().isEmpty()) return false;

        if (modeInit){
            unlock = true;
            return true;
        }

        if (loadMyData.encode(pass).trim().equals(data.get("PASS").trim())){
            unlock = true;
            return true;
        }

        Toast.makeText(myContext, "Unlock pass not valid", Toast.LENGTH_LONG).show();

        unlock = false;
        return false;
    }


    public Boolean stopIsOk(String pass){

        if (myActivity.isMyServiceRunning(ServiceLock.class) && unlock(pass)) return true;

        return false;
    }


    public void loadSettings(){
        //Chargement des paramétres de configuration
        ManageSettings loadMyData = new ManageSettings();
        HashMap<String, String> data = new HashMap<String, String>();
        loadMyData.restoreData(myContext, data);

        if (data.get("TEL") == null && data.get("PASS") == null){
            modeInit = true;
            unlock = true;
        }

        pass = data.get("PASS");

        tel = data.get("TEL");

        ManageSettings.isRecord = Boolean.parseBoolean(data.get("RECORD"));

        Log.d("Controller", "loadSettings modeInit = "+modeInit);
        Log.d("Controller", "loadSettings unlock = "+unlock);
        Log.d("Controller", "loadSettings pass = "+pass);
        Log.d("Controller", "loadSettings tel = "+tel);
        Log.d("Controller", "loadSettings ManageSettings.isRecord = "+ManageSettings.isRecord);
    }


    public boolean broadCastMessage(String msg){

        Log.d("MainActivity", "Broadcasting message");

        Intent intent = new Intent("lockMyPhone");
        intent.putExtra("message", msg);

        return LocalBroadcastManager.getInstance(myContext).sendBroadcast(intent);
    }


    public boolean saveSettings(String tel, String pass){
        HashMap<String, String> data = new HashMap<String, String>();

        if (tel == null || pass == null) return false;

        if (tel.trim().isEmpty() || pass.trim().isEmpty()) return false;

        data.put("TEL", tel);
        data.put("PASS", pass);

        data.put("RECORD", String.valueOf(ManageSettings.isRecord));

        manageData.saveData(myContext, data);

        return true;
    }



    public boolean isStartOk(String tel, String pass){
        if (tel == null || pass == null) return false;

        if (tel.trim().isEmpty() || pass.trim().isEmpty()) return false;

        //Première utilisation du soft
        if (modeInit){
            saveSettings(tel, pass);
            modeInit = false;

            if (!myActivity.isMyServiceRunning(ServiceLock.class)){
                Intent intent = new Intent(myContext, ServiceLock.class);
                myContext.startService(intent);
                return true;
            }

            return false;
        }

        //Débloqué?
        if (!unlock){
            return false;
        }

        //ihm débloqué, test passwd?
        if (unlock(pass) && !myActivity.isMyServiceRunning(ServiceLock.class)){
            saveSettings(tel, pass);
            Intent intent = new Intent(myContext, ServiceLock.class);
            myContext.startService(intent);
            return true;
        }

        return false;

    }


    public boolean onPauseOk(String tel, String pass){
        if (unlock){
            saveSettings(tel, pass);
            return true;
        }

        return false;
    }

}
