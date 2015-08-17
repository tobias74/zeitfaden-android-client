package zeitfaden.com.zeitfaden.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.message.BasicNameValuePair;

import zeitfaden.com.zeitfaden.R;
import zeitfaden.com.zeitfaden.SpinnerIntegerItem;
import zeitfaden.com.zeitfaden.services.ZeitfadenServerService;


public class SettingsActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner)findViewById(R.id.recording_interval_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new SpinnerIntegerItem[] {
            new SpinnerIntegerItem(30,"30 seconds"),
            new SpinnerIntegerItem(60,"60 seconds"),
            new SpinnerIntegerItem(120,"120 seconds")

        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        SpinnerIntegerItem item = (SpinnerIntegerItem)spinner.getSelectedItem();
        Log.d("Tobias", "we have " + item.getValue());

    }

    public void onNothingSelected(AdapterView<?> parent){

    }



}
