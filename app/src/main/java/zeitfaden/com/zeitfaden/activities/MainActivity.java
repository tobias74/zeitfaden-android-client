package zeitfaden.com.zeitfaden.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import zeitfaden.com.zeitfaden.R;
import zeitfaden.com.zeitfaden.services.ZeitfadenServerService;

import com.zeitfaden.services.web.WebRequestRunnable;
import com.zeitfaden.services.web.ZeitfadenWebService;
import com.zeitfaden.services.web.ZeitfadenWebServiceBinder;


public class MainActivity extends ActionBarActivity {

    ZeitfadenWebServiceBinder webServiceBinder;

    private ServiceConnection webServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            webServiceBinder = (ZeitfadenWebServiceBinder) service;
            Log.d("Tobias", "we got the webservice conncted");
            Log.d("Tobias", webServiceBinder.hello());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onClickTobias(View Button){
        Log.d("Tobias","got the click on test");
        Handler myHandler = new Handler();
        WebRequestRunnable myRunnable = new WebRequestRunnable();
        webServiceBinder.requestHelloWorld("Tobias", myHandler, myRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, ZeitfadenWebService.class), webServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onClickShowMap(View Button){
        final Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }

    public void onClickShowTest(View Button){
        final Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void onClickSettings(View Button){
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void onClickUploadStations(View Button){
        ZeitfadenServerService.startActionUpload(this);
    }

}
