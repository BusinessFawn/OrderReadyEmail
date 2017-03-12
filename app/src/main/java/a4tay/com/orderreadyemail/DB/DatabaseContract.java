package a4tay.com.orderreadyemail.DB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by johnkonderla on 2/12/17.
 */

public class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "xyz.4tay.pingme";


    //Preferences
    public static final String TABLE_PREFERENCES = "prefs";
    public static final class TablePreferences implements BaseColumns {
        public static final String COL_ID = "_id";
        public static final String COL_PREF_TYPE = "prefType";
        public static final String COL_PREF_VALUE = "prefValue";
    }
    public static final String DEFAULT_SORT_PREFS = TablePreferences.COL_ID;
    public static final Uri PREFERENCES_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_PREFERENCES)
            .build();
}
