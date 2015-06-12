package zeitfaden.com.zeitfaden;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class GeoPositionService extends Service implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

    private Location myLocation;
    private Handler activityCallbackHandler;

    public final IBinder mGpsBinder = new GeoPositionServiceBinder();

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL = 5000; // 5 Sekunden
    private static final long SCHNELLSTES_INTERVAL = 1000; // 1 Sekunde

    public GeoPositionService() {
    }

    @Override
    public void onLocationChanged(Location location){
        Log.d("Tobias","location changed oLocationChanged handelr got called.");
        if (location != null) {
            myLocation = location;

            if (activityCallbackHandler != null){
                final Bundle bundle = new Bundle();
                bundle.putParcelable("location",myLocation);


                final Message msg =  new Message();
                msg.setData(bundle);

                activityCallbackHandler.sendMessage(msg);

            }



        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Tobias","location service created.");

        boolean usePlayService = isGooglePlayServiceAvailable();
        if (usePlayService) {
            Log.i("TOBIAS", "onCreate(): verwende die neue Location API mit dem Fuse-Provider");
            startGeoProvider();
            if (mLocationClient != null) {
                mLocationClient.connect();
            }

        }

    }


    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
        super.onDestroy();
    }



    private void startGeoProvider(){
        Log.d("Tobias","inside startGeoProvider");
        mLocationClient = new LocationClient(this,this,this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(SCHNELLSTES_INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mGpsBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){

        return super.onUnbind(intent);
    }

    public class GeoPositionServiceBinder extends Binder {

        //public GpsData getGpsData(){
        //    return mGpsData;
        //}

        public void sendGeoPosition(){

        }

        public void addActivityCallbackHandler(final Handler callback){
            activityCallbackHandler = callback;
        }

    }

    private boolean isGooglePlayServiceAvailable() {
        int errorCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }


    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        Log.d("Tobias","connection failed to locationthingy.");

    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.d("Tobias","onConnected here in the geopostion srvice, now requesting location updates.");
        myLocation = mLocationClient.getLastLocation();
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub

    }


}
