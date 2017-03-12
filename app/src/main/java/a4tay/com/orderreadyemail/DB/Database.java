package a4tay.com.orderreadyemail.DB;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by johnkonderla on 2/12/17.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "pingMe.db";
    public static final int DB_VERSION = 1;

    private static final String LOG_TAG = Database.class.getSimpleName();
    //Preferences table
    private static final String SQL_CREATE_TABLE_PREFERENCES = "CREATE TABLE " +
            DatabaseContract.TABLE_PREFERENCES + " (" +
            DatabaseContract.TablePreferences.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DatabaseContract.TablePreferences.COL_PREF_TYPE + " INTEGER," +
            DatabaseContract.TablePreferences.COL_PREF_VALUE + " TEXT" + " )";


    private Resources resources;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        resources = context.getResources();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PREFERENCES);

        Log.d(LOG_TAG,"The onCreate method for the DB was called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_PREFERENCES);

        Log.d(LOG_TAG,"On upgrade..");
        onCreate(db);

    }
}
