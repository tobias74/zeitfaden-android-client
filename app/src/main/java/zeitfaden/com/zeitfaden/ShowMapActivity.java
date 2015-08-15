package zeitfaden.com.zeitfaden;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;

import java.lang.ref.WeakReference;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class ShowMapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private static Handler myGeoCallbackHandler;
    private static Handler myMusicCallbackHandler;



    private boolean areServicesStarted = false;
    private GoogleMap myMap;
    private Marker myMarker;

    private PowerManager.WakeLock wakeLock;


    private ServiceConnection mGeoPositionServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ((GeoPositionService.GeoPositionServiceBinder) binder).addActivityCallbackHandler(myGeoCallbackHandler);
        }

        public void onServiceDisconnected(ComponentName className){}
    };

    private ServiceConnection mMusicTrackingServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ((MusicTrackingService.MusicTrackingServiceBinder) binder).addActivityCallbackHandler(myMusicCallbackHandler);
        }

        public void onServiceDisconnected(ComponentName className){}
    };




    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("Tobias","onMapReady!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Creating Marker now.");

        myMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        myMap = map;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myGeoCallbackHandler = new ShowMapCallbackHandler(this);

        myMusicCallbackHandler = new MusicTrackingCallbackHandler(this);

        bindService(new Intent(this, GeoPositionService.class), mGeoPositionServiceConnection, 0);
        bindService(new Intent(this, MusicTrackingService.class), mMusicTrackingServiceConnection, 0);



    }

    @Override
    protected void onDestroy() {

        unbindService(mGeoPositionServiceConnection);
        unbindService(mMusicTrackingServiceConnection);

        // no, we do not want to give up any of our services.
        /*
        wakeLock.release();

        myGeoCallbackHandler.removeCallbacksAndMessages(null);
        unbindService(mGeoPositionServiceConnection);
        stopService(new Intent(this, GeoPositionService.class));

        myMusicCallbackHandler.removeCallbacksAndMessages(null);
        unbindService(mMusicTrackingServiceConnection);
        stopService(new Intent(this, MusicTrackingService.class));
        */


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


    public void handleMusicMessage(Message msg){
        final Bundle bundle = msg.getData();
        final Location location = (Location) bundle.get("location");
        Log.d("TOBIAS", "Dies ist die Location fuer die Musik " + location.toString());
    }

    public void handleMessage(Message msg) {
        final Bundle bundle = msg.getData();
        final Location location = (Location) bundle.get("location");
        Log.d("TOBIAS","Dies ist die Location " + location.toString());

        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        myMarker.setPosition(latLng);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


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



    static class MusicTrackingCallbackHandler extends Handler {
        private WeakReference<ShowMapActivity> mActivity;

        MusicTrackingCallbackHandler(ShowMapActivity activity) {
            mActivity = new WeakReference<ShowMapActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShowMapActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMusicMessage(msg);
            }
        }
    }


    public void onClickStartLocationRecording(View button){
        Log.d("Tobias","got the click on start");

        //PowerManager mgr = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        //wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        //wakeLock.acquire();

        final Intent geoIntent = new Intent(this, GeoPositionService.class);
        startService(geoIntent);

        final Intent musicIntent = new Intent(this, MusicTrackingService.class);
        startService(musicIntent);





    }


    public void onClickStopLocationRecording(View button){
        Log.d("Tobias","got the click on stop");

        stopService(new Intent(this, GeoPositionService.class));

        stopService(new Intent(this,MusicTrackingService.class));

    }
}
