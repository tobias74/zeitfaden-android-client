package zeitfaden.com.zeitfaden.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zeitfaden.com.zeitfaden.DatabaseManager;

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
    private static final String ACTION_LOGIN = "zeitfaden.com.zeitfaden.action.LOGIN";
    private static final String ACTION_TESTHELLO = "zeitfaden.com.zeitfaden.action.TESTHELLO";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "zeitfaden.com.zeitfaden.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "zeitfaden.com.zeitfaden.extra.PARAM2";

    private static final String ZEITFADEN_BASE_URL = "https://www.zeitfaden.com/api";
    private static final String ZEITFADEN_USER_LOGIN = "/user/login";
    private static final String ZEITFADEN_INSERT_STATION = "/station/insert";
    private static final String ZEITFADEN_LOGIN_OAUTH2 = "/OAuth2/token";


    private DefaultHttpClient httpClient;

    private AccountManager mAccountManager;




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

    public static void startActionNewHello(Context context) {
        Intent intent = new Intent(context, ZeitfadenServerService.class);
        intent.setAction(ACTION_TESTHELLO);
        context.startService(intent);

    }

    public static void startActionLogin(Context context, String email, String password){
        Intent intent = new Intent(context, ZeitfadenServerService.class);
        intent.setAction(ACTION_LOGIN);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        context.startService(intent);

    }


    public ZeitfadenServerService() {
        super("ZeitfadenServerService");
        httpClient = new DefaultHttpClient();

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
            } else if (ACTION_TESTHELLO.equals(action)) {
                handleNewHello();
            }
        }
    }




    private void handleNewHello(){
        Log.i("Tobias","within the handle...");
        Log.i("Tobias","now in the webserncie sending the new hello world.");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String idToken = settings.getString("idToken", "");


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.zeitfaden.com/api/user/authenticated")
                .header("Authorization", "Bearer " + idToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.i("Tobias http request",response.message());
            Log.i("Tobias",response.body().string());
        } catch (IOException e) {
            Log.i("Tobias Exception","Somethign wrong inthe htttp reuqest");
            e.printStackTrace();
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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String idToken = settings.getString("idToken", "");

        Log.d("Tobias","this is the idToken we are goona use to upload " + idToken);

        OkHttpClient client = new OkHttpClient();


        DatabaseManager myDatabaseManager = DatabaseManager.getInstance(this);

        Cursor stationCursor = myDatabaseManager.getReadCursorOnStations();


        while (stationCursor.moveToNext()){
            Log.d("Tobias", stationCursor.getString(0));

            String myId = stationCursor.getString(0);
            String publishStatus = "public"; //stationCursor.getString(1);
            Double startLatitude = stationCursor.getDouble(2);
            Double endLatitude = stationCursor.getDouble(3);
            Double startLongitude = stationCursor.getDouble(4);
            Double endLongitude = stationCursor.getDouble(5);
            Long startTimestamp = stationCursor.getLong(6);
            Long endTimestamp = stationCursor.getLong(7);



            RequestBody requestBody = new FormBody.Builder()
                    .add("action", "login")
                    .add("startLatitude", String.valueOf(startLatitude))
                    .add("startLongitude", String.valueOf(startLongitude))
                    .add("endLatitude", String.valueOf(endLatitude))
                    .add("endLongitude", String.valueOf(endLongitude))
                    .add("startTimestamp", String.valueOf(startTimestamp))
                    .add("endTimestamp", String.valueOf(endTimestamp))
                    .add("publishStatus", String.valueOf(publishStatus))
                    .build();

            Request request = new Request.Builder()
                    .url(ZEITFADEN_BASE_URL + ZEITFADEN_INSERT_STATION)
                    .header("Authorization", "Bearer " + idToken)
                    .post(requestBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                Log.i("Tobias http request",response.message());
                Log.i("Tobias",response.body().string());
                if (response.code() == 200){
                    myDatabaseManager.getWritableDatabase().delete("stations","_id=?",new String[]{myId});
                }
                else {
                    Log.d("Tobias", "WE had an upload error");
                }
            } catch (IOException e) {
                Log.i("Tobias Exception","Somethign wrong inthe htttp reuqest");
                e.printStackTrace();
            }



        }




    }


}
