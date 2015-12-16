package com.zeitfaden.services.web;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ZeitfadenWebService extends Service {

    private final IBinder myBinder = new ZeitfadenWebServiceBinder();

    public ZeitfadenWebService() {
    }


    public void onCreate(){
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


}
