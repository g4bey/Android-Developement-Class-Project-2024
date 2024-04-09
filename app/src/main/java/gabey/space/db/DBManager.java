package gabey.space.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import gabey.space.model.Serie;
import gabey.space.utils.StringUtils;


/*
    Singleton instantiating the connection to the DB through the DbHandler.
    Provides an API to handle common requests.
 */

public class DBManager {
    private final String TAG = "OriginalDB@DBManager";
    private SQLiteDatabase readOnlyDb;
    private SQLiteDatabase readWriteDb;
    private DBHandler dbHandler;
    private static DBManager dbManager;

    // this constructor should never be used.
    private DBManager(){}

    /*
        This contructor is private and getInstance(ctx) should be used.
        Creates the DbHandler and instantiates database connections.
     */
    private DBManager(Context ctx) throws SQLException {
        dbHandler = new DBHandler(ctx);
        readWriteDb = dbHandler.getWritableDatabase();
        readOnlyDb = dbHandler.getReadableDatabase();
    }

    /*
        Returns the dbManager connection and makes sure there's only one.
     */
    public static DBManager getInstance(Context ctx) {
        if (dbManager == null) {
            dbManager = new DBManager(ctx);
        }

        return dbManager;
    }

    /*
        Closes the connection.
     */
    public void close() {
        readOnlyDb.close();
        readWriteDb.close();
        dbHandler.close();
    }

    public boolean showIsFaved(int id) {
        final String query = "SELECT * FROM "
                + DBContract.FavoriteSeries.TABLE_NAME
                + " WHERE api_id = ?";

        Cursor c = readOnlyDb.rawQuery(
                query,
                new String[]{String.valueOf(id)}
        );
        return c.getCount() == 1;
    }

    public void favThisSerie(Serie serie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.FavoriteSeries.COLUMN_API_ID, serie.getId());
        contentValues.put(DBContract.FavoriteSeries.COLUMN_IMG, serie.getImg());
        contentValues.put(DBContract.FavoriteSeries.COLUMN_NAME, serie.getName());
        contentValues.put(DBContract.FavoriteSeries.COLUMN_SUMMARY, serie.getSummary());
        contentValues.put(DBContract.FavoriteSeries.COLUMN_GENRES, StringUtils.joinAsString(serie.getGenres(), ", "));

        readWriteDb.insert(
                DBContract.FavoriteSeries.TABLE_NAME,
                null,
                contentValues
        );
    }

    public void unfavThisSerie(Serie serie) {
        readWriteDb.delete(
                DBContract.FavoriteSeries.TABLE_NAME,
                DBContract.FavoriteSeries.COLUMN_API_ID + " = ?",
                new String[]{String.valueOf(serie.getId())}
        );
    }
}
