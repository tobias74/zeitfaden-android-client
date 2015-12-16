package com.zeitfaden.services.web;

import android.os.Binder;
import android.os.Handler;

/**
 * Created by tobias on 16.12.15.
 */
public class ZeitfadenWebServiceBinder extends Binder {

    public String hello(){
        return "hello there";
    }

    public void requestHelloWorld(String name, final Handler handler, final WebRequestRunnable myRunnable){
        new Thread(){
            public void run(){
                myRunnable.result = "Hello from the thread!";
                handler.post(myRunnable);
            }
        }.start();
    }

}
