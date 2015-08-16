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
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }


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
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClickExampleTwo(View Button){
        final Intent intent = new Intent(this, Example2Activity.class);
        startActivity(intent);
    }

    public void onClickUploadStations(View Button){
        ZeitfadenServerService.startActionUpload(this);
    }

}
