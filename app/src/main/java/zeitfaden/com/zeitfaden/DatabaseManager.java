package zeitfaden.com.zeitfaden;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Tobias on 09.07.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zeitfaden.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseManager sINSTANCE;
    private static Object sLOCK;

    public static final String SQL_CREATE_STATIONS_TABLE =
        "CREATE TABLE stations (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "description TEXT, " +
        "publish_status TEXT, " +
        "start_latitude REAL, " +
        "end_latitude REAL, " +
        "start_longitude REAL, " +
        "end_longitude REAL, " +
        "start_timestamp INTEGER" +
        "end_timestamp INTEGER" +
        "media_file_path TEXT" +
        ");";



    public DatabaseManager(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);


    }


    public static DatabaseManager getInstance(Context context){
        Log.d("Tobias", "inside the get Instace method.");

        if (sINSTANCE == null){
            Log.d("Tobias", "instance null.");
            if (sINSTANCE == null && context != null){
                Log.d("Tobias", "doing it in the deep.");
                sINSTANCE = new DatabaseManager(context.getApplicationContext());
            }
        }
        return sINSTANCE;
    }


    public void storeStation(Station myStation){
        Log.d("Tobias", "inserting new station in DatabaseMansger");

        SQLiteStatement stationInsert = this.getWritableDatabase().compileStatement("INSERT INTO stations (description) VALUES (?)");

        stationInsert.bindString(1, myStation.getDescription());

        long id = stationInsert.executeInsert();


    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(DatabaseManager.SQL_CREATE_STATIONS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }



}
