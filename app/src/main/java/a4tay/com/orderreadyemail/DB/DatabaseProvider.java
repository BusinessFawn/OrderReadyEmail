package a4tay.com.orderreadyemail.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by johnkonderla on 2/12/17.
 */

public class DatabaseProvider extends ContentProvider {

    public static final int PREFERENCES_TABLE = 100;

    private static final String LOG_TAG = DatabaseProvider.class.getSimpleName();

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_PREFERENCES, PREFERENCES_TABLE);

    }

    private Database database;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {return null;}

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder liteQueryBuilder = new SQLiteQueryBuilder();



        switch (uriMatcher.match(uri)) {

            case PREFERENCES_TABLE:
                liteQueryBuilder.setTables(DatabaseContract.TABLE_PREFERENCES);
                Log.d(LOG_TAG,"Selecting from preferences");
                break;

            default:
                Log.d(LOG_TAG,"NO 'where' was given");
                break;
        }

        if(sortOrder == null || sortOrder.equals("")) {
            sortOrder = DatabaseContract.DEFAULT_SORT_PREFS;
        }

        db = database.getReadableDatabase();
        Cursor cursor = liteQueryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = database.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            default:
                id = db.insert(DatabaseContract.TABLE_PREFERENCES,"",values);
                db.close();
                Log.d(LOG_TAG,"I inserted a pref!");
                return ContentUris.withAppendedId(DatabaseContract.PREFERENCES_URI,id);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = database.getWritableDatabase();
        int id;
        switch (uriMatcher.match(uri)) {
            default:
                id = db.delete(DatabaseContract.TABLE_PREFERENCES, selection, selectionArgs);
                db.close();
                Log.d(LOG_TAG, "I inserted a pref! why did I have to do this?");
                break;
        }
        return id;


    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = database.getWritableDatabase();

        int id;
        switch (uriMatcher.match(uri)) {
            default:
                id= db.update(DatabaseContract.TABLE_PREFERENCES,values,selection,selectionArgs);
                Log.d(LOG_TAG,"Updated Preferences Table");
                break;
        }
        return id;
    }
}
