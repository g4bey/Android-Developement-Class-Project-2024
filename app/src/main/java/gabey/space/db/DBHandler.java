package gabey.space.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

/*
    Handles Database Management (Creation, Deletion, Upgrade...) on a global level.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = DBContract.DATABASE_NAME;
    private static final int DATABASE_VERSION = 2;

    private final String TAG = "OriginalDB@DBHandler";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        Creates :
            - FavoriteSeries
            - SearchHistory
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "";

        // Creating table holding favorites.
        Log.i(TAG, "Creating Table: " + DBContract.FavoriteSeries.TABLE_NAME);
        query = "CREATE TABLE " + DBContract.FavoriteSeries.TABLE_NAME + "("
                + DBContract.FavoriteSeries._ID + " INTEGER PRIMARY KEY,"
                + DBContract.FavoriteSeries.COLUMN_API_ID + " TEXT,"
                + DBContract.FavoriteSeries.COLUMN_NAME + " TEXT,"
                + DBContract.FavoriteSeries.COLUMN_GENRES + " TEXT,"
                + DBContract.FavoriteSeries.COLUMN_SUMMARY + " TEXT,"
                + DBContract.FavoriteSeries.COLUMN_IMG + " TEXT" + ")";
        db.execSQL(query);

        // Creating table holding search history
        Log.i(TAG, "Creating Table: " + DBContract.SearchHistory.TABLE_NAME);
        query =  "CREATE TABLE " + DBContract.SearchHistory.TABLE_NAME + " (" +
                DBContract.SearchHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.SearchHistory.COLUMN_query + " TEXT" + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading Database");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i(TAG, "Opening Database");
    }

}
