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
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
    private DatabaseManager myDatabaseManager;

    private long previousTimestamp = 0;
    private long currentTimestamp = 0;

    private double previousLatitude = 0;
    private double previousLongitude = 0;

    private ServiceConnection mGeoPositionServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ((GeoPositionService.GeoPositionServiceBinder) binder).addActivityCallbackHandler(myGeoCallbackHandler);
        }

        public void onServiceDisconnected(ComponentName className){}
    };

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("Tobias","onMapReady!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Creating Marker now.");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myGeoCallbackHandler = new ShowMapCallbackHandler(this);




        final Intent geoIntent = new Intent(this, GeoPositionService.class);
        bindService(geoIntent, mGeoPositionServiceConnection, Context.BIND_AUTO_CREATE);


        Log.d("Tobias", "short before the get instance method inside show map acitivty.");
        myDatabaseManager = DatabaseManager.getInstance(this);

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

    public void handleMessage(Message msg) {
        final Bundle bundle = msg.getData();
        final Location location = (Location) bundle.get("location");
        Log.d("TOBIAS","Dies ist die Location " + location.toString());

        if (previousLatitude == 0)
        {
            previousLatitude = location.getLatitude();
            previousLongitude = location.getLongitude();
        }

        if (previousTimestamp == 0)
        {
            previousTimestamp = System.currentTimeMillis()/1000;
        }

        currentTimestamp = System.currentTimeMillis()/1000;

        Station myStation = new Station();
        myStation.setDescription("#radtour #grandesalpes");
        myStation.setStartLatitude(previousLatitude);
        myStation.setStartLongitude(previousLongitude);
        myStation.setEndLatitude(location.getLatitude());
        myStation.setEndLongitude(location.getLongitude());
        myStation.setPublishStatus("public");
        myStation.setStartTimestamp(previousTimestamp);
        myStation.setEndTimestamp(currentTimestamp);
        Log.d("Tobias","stations start latitude " + myStation.getStartLatitude());

        myDatabaseManager.storeStation(myStation);

        previousLatitude = location.getLatitude();
        previousLongitude = location.getLongitude();
        previousTimestamp = currentTimestamp;


        /*
        final Bundle bundle = msg.getData();
        if (bundle != null) {
            final Location location = (Location) bundle
                    .get("location");
            final LatLng latLng = new LatLng(location.getLatitude(),
                    location.getLongitude());

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        }
        */
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
