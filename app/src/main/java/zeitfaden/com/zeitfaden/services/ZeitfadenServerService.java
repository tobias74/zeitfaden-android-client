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

import zeitfaden.com.zeitfaden.DatabaseManager;
import zeitfaden.com.zeitfaden.authentication.AccountGeneral;

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

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "zeitfaden.com.zeitfaden.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "zeitfaden.com.zeitfaden.extra.PARAM2";

    private static final String ZEITFADEN_BASE_URL = "http://api.zeitfaden.com";
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
            } else if (ACTION_LOGIN.equals(action)) {
                handleActionLogin(intent.getStringExtra("email"), intent.getStringExtra("password"));
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

    private void handleActionLogin(String email, String password){
        try {

            DefaultHttpClient client = new DefaultHttpClient();
            String loginUrl = ZEITFADEN_BASE_URL + ZEITFADEN_LOGIN_OAUTH2;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("username",email));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("grant_type","password"));
            nameValuePairs.add(new BasicNameValuePair("client_id","5612d33d6987c887945"));
            HttpPost loginRequest = new HttpPost(loginUrl);

            loginRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse loginResponse = client.execute(loginRequest);

            String loginResponseString = EntityUtils.toString(loginResponse.getEntity());
            Log.d("Tobias", loginResponseString);
            JSONObject json = new JSONObject(loginResponseString);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("access_token", json.getString("access_token"));
            editor.putString("refresh_token", json.getString("refresh_token"));
            editor.commit();

        }
        catch (IOException e){
            Log.e("Tobias","something wrong here.");
        }
        catch (JSONException el){
            Log.d("Tobias", "json expcetion");
        }



    }



    private void handleActionUpload(){


        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String access_token = ""; // = settings.getString("access_token", "");

        mAccountManager = AccountManager.get(getBaseContext());
        Account myAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

        try {
            access_token = mAccountManager.blockingGetAuthToken(myAccounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }

        Log.d("Tobias","this is the access_token we are goona use to upload " + access_token);

        DatabaseManager myDatabaseManager = DatabaseManager.getInstance(this);

        Cursor stationCursor = myDatabaseManager.getReadCursorOnStations();


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


            HttpPost httpPost = new HttpPost(ZEITFADEN_BASE_URL + ZEITFADEN_INSERT_STATION);

            Log.d("Tobias", "trying3 to upload here with start latitude " + startLatitude);

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();


            postParameters.add(new BasicNameValuePair("description", String.valueOf(description)));
            postParameters.add(new BasicNameValuePair("startLatitude", String.valueOf(startLatitude)));
            postParameters.add(new BasicNameValuePair("startLongitude", String.valueOf(startLongitude)));
            postParameters.add(new BasicNameValuePair("endLatitude", String.valueOf(endLatitude)));
            postParameters.add(new BasicNameValuePair("endLongitude", String.valueOf(endLongitude)));
            postParameters.add(new BasicNameValuePair("startTimestamp", String.valueOf(startTimestamp)));
            postParameters.add(new BasicNameValuePair("endTimestamp", String.valueOf(endTimestamp)));
            postParameters.add(new BasicNameValuePair("publishStatus", String.valueOf(publishStatus)));
            postParameters.add(new BasicNameValuePair("access_token", access_token));

            try{
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            }
            catch (UnsupportedEncodingException e2){

            }

            Log.d("Tobias", "trying4 to upload here.");

            try {
                Log.d("Tobias", "trying5 to upload here.");

                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse uploadResponse = client.execute(httpPost);

                Log.d("Tobias", "trying6 to upload here.");
                if (uploadResponse.getStatusLine().getStatusCode() == 200){
                    myDatabaseManager.getWritableDatabase().delete("stations","_id=?",new String[]{myId});
                }
            }
            catch (ClientProtocolException e1){

            }
            catch (IOException e1){

            }

        }




    }


}
