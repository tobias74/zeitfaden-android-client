package zeitfaden.com.zeitfaden;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class GeoPositionService extends Service implements LocationListener {

    private Location myLocation;
    private Handler activityCallbackHandler;

    public final IBinder mGpsBinder = new GeoPositionServiceBinder();

    public GeoPositionService() {
    }

    @Override
    public void onStatusChanged(String what, int ding, Bundle some){

    }

    @Override
    public void onProviderEnabled(String what){

    }

    @Override
    public void onProviderDisabled(String what){

    }

    @Override
    public void onLocationChanged(Location location){
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
