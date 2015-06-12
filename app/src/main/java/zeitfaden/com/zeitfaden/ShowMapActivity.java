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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapView;

import java.lang.ref.WeakReference;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



public class ShowMapActivity extends ActionBarActivity {

    private static Handler myGeoCallbackHandler;
    private static final float DEFAULT_ZOOM_LEVEL = 17.5f;

    private MapView mMapView;
    private GoogleMap mMap;

    private ServiceConnection mGeoPositionServiceConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ((GeoPositionService.GeoPositionServiceBinder) binder).addActivityCallbackHandler(myGeoCallbackHandler);
        }

        public void onServiceDisconnected(ComponentName className){}
    };

    private boolean isGooglePlayServiceAvailable() {
        int errorCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode, this, -1);
            if (errorDialog != null) {
                errorDialog.show();
                return false;
            }
        }
        return true;
    }
    private void initMapView() {
        Log.d("TOBIAS", "initMapView(): aufgerufen...");

        boolean usePlayService = isGooglePlayServiceAvailable();
        if (usePlayService) {
            MapsInitializer.initialize(this);

            if (mMap == null) {
                mMap = mMapView.getMap();
                if (mMap != null) {
                    Log.d("TOBIAS", "initMapView(): MapView initialisieren");
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.setIndoorEnabled(true);
                    mMap.setTrafficEnabled(true);

                    CameraUpdate camPos = CameraUpdateFactory.newLatLng(new LatLng(11.562276,104.920292));
                    mMap.moveCamera(camPos);
                    Log.d("TOBIAS", "initMapView(): MaxZoomLevel: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + mMap.getMaxZoomLevel());

                    //mMap.setOnMarkerClickListener(this);
                    //mMap.setOnCameraChangeListener(this);

                    // Default-Zoomlevel:
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL));
                }
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_map);


        myGeoCallbackHandler = new ShowMapCallbackHandler(this);

        mMapView = (MapView) findViewById(R.id.mv_show_map);
        mMapView.onCreate(savedInstanceState);

        initMapView();


        final Intent geoIntent = new Intent(this, GeoPositionService.class);
        bindService(geoIntent, mGeoPositionServiceConnection, Context.BIND_AUTO_CREATE);

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
        Log.d("TOPBIAS", "ja hier im Callback in der show map");

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
