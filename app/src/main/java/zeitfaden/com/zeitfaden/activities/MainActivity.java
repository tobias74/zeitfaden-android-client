package zeitfaden.com.zeitfaden.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import zeitfaden.com.zeitfaden.R;
import zeitfaden.com.zeitfaden.services.ZeitfadenServerService;


public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void onClickShowMap(View Button){
        final Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }

    public void onClickShowTest(View Button){
        final Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }


    public void onClickSettings(View Button){

    }

    public void onClickLogin(View Button){
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickUploadStations(View Button){
        ZeitfadenServerService.startActionUpload(this);
    }

}
