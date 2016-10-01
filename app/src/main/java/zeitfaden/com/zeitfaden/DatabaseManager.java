package zeitfaden.com.zeitfaden;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Tobias on 09.07.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zeitfaden.db";
    private static final int DATABASE_VERSION = 7;
    private static DatabaseManager sINSTANCE;
    private static Object sLOCK;

    public static final String SQL_CREATE_STATIONS_TABLE =
        "CREATE TABLE stations (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "publish_status TEXT, " +
        "latitude REAL, " +
        "longitude REAL, " +
        "speed REAL, " +
        "altitude REAL, " +
        "accuracy REAL, " +
        "tour_id TEXT, " +
        "timestamp INTEGER " +
        ");";

    public static final String SQL_DELETE_STATIONS_TABLE = "DROP TABLE IF EXISTS stations;";


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

        SQLiteStatement stationInsert = this.getWritableDatabase().compileStatement("INSERT INTO stations (latitude, longitude, timestamp, publish_status, speed, accuracy, altitude, tour_id) VALUES (?,?,?,?,?,?,?,?)");

        stationInsert.bindDouble(1, myStation.latitude);
        stationInsert.bindDouble(2, myStation.longitude);
        stationInsert.bindLong(3, myStation.timestamp);
        stationInsert.bindString(4, myStation.publishStatus);
        stationInsert.bindDouble(5, myStation.speed);
        stationInsert.bindDouble(6, myStation.accuracy);
        stationInsert.bindDouble(7, myStation.altitude);
        stationInsert.bindString(8, myStation.tourId);


        long id = stationInsert.executeInsert();


    }


    public Cursor getReadCursorOnStations(){
        Cursor allStations = this.getReadableDatabase().rawQuery("SELECT * FROM stations",null);
        return allStations;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(DatabaseManager.SQL_CREATE_STATIONS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d("Tobias","now dropping the old table.");
        db.execSQL(DatabaseManager.SQL_DELETE_STATIONS_TABLE);
        onCreate(db);
    }



}
