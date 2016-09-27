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


    public final IBinder mGpsBinder = new GeoPositionServiceBinder();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final long UPDATE_INTERVAL = 20000;
    private static final long FASTEST_INTERVAL = 10000;



    public GeoPositionService() {
    }


    private void recordNewLocation(Location location){
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

    }

    private boolean isAccurateEnough(Location newLocation) {
        float accuracy = newLocation.getAccuracy();
        float distanceBetweenOldAndNew = newLocation.distanceTo(myLocation);

        Log.d("Tobias", "distance between old and new " + Float.toString(distanceBetweenOldAndNew));

        if (distanceBetweenOldAndNew > 2.5 * accuracy) {
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
