package com.application.bach.gpstracker;

import android.content.Context;

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

        //TODO
        return false;
    }


    public Boolean stopIsOk(String pass){
        //TODO
        return false;
    }


    public void loadSettings(){
        //TODO
    }


    public boolean broadCastMessage(String msg){
        //TODO
        return false;
    }


    public boolean isStartOk(String tel, String pass){
        //TODO
        return false;
    }


    public boolean onPauseOk(String tel, String pass){
        //TODO
        return false;
    }

}
