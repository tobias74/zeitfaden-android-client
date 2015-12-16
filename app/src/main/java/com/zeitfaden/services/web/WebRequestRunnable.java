package com.zeitfaden.services.web;

import android.util.Log;

/**
 * Created by tobias on 16.12.15.
 */
public class WebRequestRunnable implements Runnable {

    public String result = "nothing yet";

    @Override
    public void run() {
        Log.d("Tobias", "inside the urn of the runnable " + result);
    }


}
