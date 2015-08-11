package zeitfaden.com.zeitfaden;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ZeitfadenServerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "zeitfaden.com.zeitfaden.action.FOO";
    private static final String ACTION_BAZ = "zeitfaden.com.zeitfaden.action.BAZ";
    private static final String ACTION_UPLOAD = "zeitfaden.com.zeitfaden.action.UPLOAD";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "zeitfaden.com.zeitfaden.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "zeitfaden.com.zeitfaden.extra.PARAM2";






    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ZeitfadenServerService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ZeitfadenServerService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public static void startActionUpload(Context context){
        Intent intent = new Intent(context, ZeitfadenServerService.class);
        intent.setAction(ACTION_UPLOAD);
        context.startService(intent);

    }


    public ZeitfadenServerService() {
        super("ZeitfadenServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            } else if (ACTION_UPLOAD.equals(action)) {
                handleActionUpload();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionUpload(){

        Log.d("Tobias", "trying to upload here.");
        String myInsertUrl = "http://test-api.zeitfaden.com:80/station/insert/";
        String myLoginUrl = "http://test-api.zeitfaden.com:80/user/login/";

        DefaultHttpClient client = new DefaultHttpClient();
        Log.d("Tobias", "trying2 to upload here.");

        
        HttpPost loginHttpPost = new HttpPost(myLoginUrl);
        List<NameValuePair> loginParameters = new ArrayList<NameValuePair>();
        loginParameters.add(new BasicNameValuePair("email", "***********"));
        loginParameters.add(new BasicNameValuePair("password", "*********"));
        try{
            loginHttpPost.setEntity(new UrlEncodedFormEntity(loginParameters));
        }
        catch (UnsupportedEncodingException e2){

        }

        try {
            Log.d("Tobias", "trying5 to login here.");
            client.execute(loginHttpPost);
            Log.d("Tobias", "trying6 to login here.");
        }
        catch (ClientProtocolException e1){

        }
        catch (IOException e1){

        }



        DatabaseManager myDatabaseManager = DatabaseManager.getInstance(this);

        Cursor stationCursor = myDatabaseManager.getReadCursorOnStations();


/*
        "description TEXT, " +
        "publish_status TEXT, " +
        "start_latitude REAL, " +
        "end_latitude REAL, " +
        "start_longitude REAL, " +
        "end_longitude REAL, " +
        "start_timestamp INTEGER, " +
        "end_timestamp INTEGER, " +
 */
        while (stationCursor.moveToNext()){
            Log.d("Tobias", stationCursor.getString(0));
            Log.d("Tobias", stationCursor.getString(1));

            String myId = stationCursor.getString(0);
            String description = stationCursor.getString(1);
            String publishStatus = "public"; //stationCursor.getString(2);
            Double startLatitude = stationCursor.getDouble(3);
            Double endLatitude = stationCursor.getDouble(4);
            Double startLongitude = stationCursor.getDouble(5);
            Double endLongitude = stationCursor.getDouble(6);
            Long startTimestamp = stationCursor.getLong(7);
            Long endTimestamp = stationCursor.getLong(8);


            HttpPost httpPost = new HttpPost(myInsertUrl);

            Log.d("Tobias", "trying3 to upload here with start latitude " + startLatitude);

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();


            postParameters.add(new BasicNameValuePair("startLatitude", String.valueOf(startLatitude)));
            postParameters.add(new BasicNameValuePair("startLongitude", String.valueOf(startLongitude)));
            postParameters.add(new BasicNameValuePair("endLatitude", String.valueOf(endLatitude)));
            postParameters.add(new BasicNameValuePair("endLongitude", String.valueOf(endLongitude)));
            postParameters.add(new BasicNameValuePair("startTimestamp", String.valueOf(startTimestamp)));
            postParameters.add(new BasicNameValuePair("endTimestamp", String.valueOf(endTimestamp)));
            postParameters.add(new BasicNameValuePair("publishStatus", String.valueOf(publishStatus)));

            try{
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            }
            catch (UnsupportedEncodingException e2){

            }

            Log.d("Tobias", "trying4 to upload here.");

            try {
                Log.d("Tobias", "trying5 to upload here.");
                client.execute(httpPost);
                Log.d("Tobias", "trying6 to upload here.");
            }
            catch (ClientProtocolException e1){

            }
            catch (IOException e1){

            }


            myDatabaseManager.getWritableDatabase().delete("stations","_id=?",new String[]{myId});
        }




    }
}
