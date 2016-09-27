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
    private static final int DATABASE_VERSION = 4;
    private static DatabaseManager sINSTANCE;
    private static Object sLOCK;

    public static final String SQL_CREATE_STATIONS_TABLE =
        "CREATE TABLE stations (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "publish_status TEXT, " +
        "start_latitude REAL, " +
        "end_latitude REAL, " +
        "start_longitude REAL, " +
        "end_longitude REAL, " +
        "start_timestamp INTEGER, " +
        "end_timestamp INTEGER, " +
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

        SQLiteStatement stationInsert = this.getWritableDatabase().compileStatement("INSERT INTO stations (description, start_latitude, start_longitude, end_latitude, end_longitude, start_timestamp, end_timestamp, publish_status) VALUES (?,?,?,?,?,?,?,?)");

        stationInsert.bindDouble(1, myStation.getStartLatitude());
        stationInsert.bindDouble(2, myStation.getStartLongitude());
        stationInsert.bindDouble(3, myStation.getEndLatitude());
        stationInsert.bindDouble(4, myStation.getEndLongitude());
        stationInsert.bindLong(5, myStation.getStartTimestamp());
        stationInsert.bindLong(6, myStation.getEndTimestamp());
        stationInsert.bindString(7, myStation.getPublishStatus());


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
        //Log.d("Tobias","now dropping the old table.");
        //db.execSQL("DROP TABLE stations");
        onCreate(db);
    }



}
