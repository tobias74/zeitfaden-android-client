package zeitfaden.com.zeitfaden;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicTrackingService extends Service {

    private Handler activityCallbackHandler;

    public final IBinder mMusicBinder = new MusicTrackingServiceBinder();

    public static final String SERVICECMD = "com.android.music.musicservicecommand";


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String track = intent.getStringExtra("track");
            Log.d("Tobias", "this track is playing!!! : " + track);
        }
    };


    public MusicTrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }



    @Override
    public void onCreate(){
        super.onCreate();

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
