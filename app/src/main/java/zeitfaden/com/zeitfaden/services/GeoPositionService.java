package zeitfaden.com.zeitfaden.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.Random;

import zeitfaden.com.zeitfaden.DatabaseManager;
import zeitfaden.com.zeitfaden.Station;

public class GeoPositionService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Location myLocation;
    private Handler activityCallbackHandler;

    private DatabaseManager myDatabaseManager;

    private long previousTimestamp = 0;
    private long currentTimestamp = 0;

    private double previousLatitude = 0;
    private double previousLongitude = 0;

    private String currentTourId = "";


    public final IBinder mGpsBinder = new GeoPositionServiceBinder();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final long UPDATE_INTERVAL = 60000;
    private static final long FASTEST_INTERVAL = 5000;



    public GeoPositionService() {
    }


    private void recordNewLocation(Location location){

        // currentTimestamp = System.currentTimeMillis()/1000;

        Station myStation = new Station();
        myStation.latitude = location.getLatitude();
        myStation.longitude = location.getLongitude();
        myStation.publishStatus = "public";
        myStation.timestamp = location.getTime()/1000;
        myStation.accuracy = location.getAccuracy();
        myStation.altitude = location.getAltitude();
        myStation.speed = location.getSpeed();
        myStation.tourId = currentTourId;

        myDatabaseManager.storeStation(myStation);


    }

    private boolean isAccurateEnough(Location newLocation) {
        float accuracy = newLocation.getAccuracy();
        float distanceBetweenOldAndNew = newLocation.distanceTo(myLocation);

        Log.d("Tobias", "distance between old and new " + Float.toString(distanceBetweenOldAndNew));

        if (distanceBetweenOldAndNew > 2.3 * accuracy) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onLocationChanged(Location newLocation){
        Log.d("Tobias","location changed oLocationChanged handelr got called.");
        if (newLocation != null) {

            Log.d("Tobias", "this is the acuracy:");
            Log.d("Tobias", Float.toString(newLocation.getAccuracy()));

            if ((myLocation == null) || (isAccurateEnough(newLocation))) {
                myLocation = newLocation;

                Log.d("Tobias", "we record!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");



                recordNewLocation(myLocation);


                if (activityCallbackHandler != null){
                    final Bundle bundle = new Bundle();
                    bundle.putParcelable("location",myLocation);


                    final Message msg =  new Message();
                    msg.setData(bundle);

                    activityCallbackHandler.sendMessage(msg);

                }

            } else {
                Log.d("Tobias", "no, we did not record, that is also a good thing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Tobias", "location service created.");

        Log.d("Tobias", "short before the get instance method inside show map acitivty.");
        myDatabaseManager = DatabaseManager.getInstance(this);

        Random r = new Random();
        currentTourId = "tour_id_" + String.valueOf(System.currentTimeMillis());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL); // Update location every second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TOBIAS", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("TOBIAS", "GoogleApiClient connection has failed");
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







}
