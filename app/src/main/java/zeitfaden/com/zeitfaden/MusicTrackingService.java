package zeitfaden.com.zeitfaden;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MusicTrackingService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Handler activityCallbackHandler;

    public final IBinder mMusicBinder = new MusicTrackingServiceBinder();

    private GoogleApiClient mGoogleApiClient;


    private Location myLocation;

    public static final String SERVICECMD = "com.android.music.musicservicecommand";

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TOBIAS", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("TOBIAS", "GoogleApiClient connection has failed");
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String track = intent.getStringExtra("track");
            Log.d("Tobias", "this track is playing!!! : " + track);



            if (activityCallbackHandler != null){
                final Bundle bundle = new Bundle();
                bundle.putParcelable("location", LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
                bundle.putString("track",track);


                final Message msg =  new Message();
                msg.setData(bundle);

                activityCallbackHandler.sendMessage(msg);

            }

        }
    };


    @Override
    public void onConnected(Bundle connectionHint) {
        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("Tobias","this is the location from the music service " + myLocation.toString());

    }


    public MusicTrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }



    @Override
    public void onCreate(){
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();



        Log.d("Tobias","Ok this is inside the new music service. on creatre.");
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");

        iF.addAction("com.htc.music.metachanged");

        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        registerReceiver(mReceiver, iF);

    }




    public class MusicTrackingServiceBinder extends Binder {



        public void addActivityCallbackHandler(final Handler callback){
            activityCallbackHandler = callback;
        }

    }



}
