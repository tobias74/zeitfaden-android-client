package zeitfaden.com.zeitfaden;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;


public class ShowMapActivity extends ActionBarActivity {

    private static Handler myGeoCallbackHandler;

    private ServiceConnection mGeoPositionServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ((GeoPositionService.GeoPositionServiceBinder) binder).addActivityCallbackHandler(myGeoCallbackHandler);
        }

        public void onServiceDisconnected(ComponentName className){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGeoCallbackHandler = new ShowMapCallbackHandler(this);
        final Intent geoIntent = new Intent(this, GeoPositionService.class);
        bindService(geoIntent, mGeoPositionServiceConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_show_map);
    }

    @Override
    protected void onDestroy() {
        myGeoCallbackHandler.removeCallbacksAndMessages(null);
        unbindService(mGeoPositionServiceConnection);
        stopService(new Intent(this, GeoPositionService.class));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_map, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleMessage(Message msg){
        Log.d("TOPBIAS","ja hier im Callback in der show map");
    }

    static class ShowMapCallbackHandler extends Handler {
        private WeakReference<ShowMapActivity> mActivity;

        ShowMapCallbackHandler(ShowMapActivity activity) {
            mActivity = new WeakReference<ShowMapActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShowMapActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

}
