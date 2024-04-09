package gabey.space.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /*
        True if the api_id is in the database.
     */
    public boolean showIsFaved(int id) {
        Log.i(TAG, "checking if " + id + " is faved");
        final String query = "SELECT * FROM "
                + DBContract.FavoriteSeries.TABLE_NAME
                + " WHERE api_id = ?";

        Cursor c = readOnlyDb.rawQuery(
                query,
                new String[]{String.valueOf(id)}
        );

        int count = c.getCount();
        c.close();
        return count != 0;
    }

    /*
        Add information about the serie to the database.
     */
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

        Log.i(TAG, "Fav persisted into database");
    }

    public void unfavThisSerie(Serie serie) {
        readWriteDb.delete(
                DBContract.FavoriteSeries.TABLE_NAME,
                DBContract.FavoriteSeries.COLUMN_API_ID + " = ?",
                new String[]{String.valueOf(serie.getId())}
        );
        Log.i(TAG, "Unfav persisted into database");
    }

    /*
        Return all the faved as "Serie" objects.
     */
    public ArrayList<Serie> getFavedSeries() {
        final String query = "SELECT * FROM "
                + DBContract.FavoriteSeries.TABLE_NAME;

        return fetchSeries(query);
    }

    private ArrayList<Serie> fetchSeries(String query) {
        ArrayList<Serie> series = new ArrayList<>();

        Cursor c = readOnlyDb.rawQuery(
                query, null
        );
        Log.i(TAG, "Fetching " + c.getCount()  +" series from FavoriteSeries");

        while (c.moveToNext()) {
            int index_id = c.getColumnIndex(DBContract.FavoriteSeries.COLUMN_API_ID);
            int index_img = c.getColumnIndex(DBContract.FavoriteSeries.COLUMN_IMG);
            int index_name = c.getColumnIndex(DBContract.FavoriteSeries.COLUMN_NAME);
            int index_summary = c.getColumnIndex(DBContract.FavoriteSeries.COLUMN_SUMMARY);
            int index_genres = c.getColumnIndex(DBContract.FavoriteSeries.COLUMN_GENRES);

            int api_id = c.getInt(index_id);
            String img = c.getString(index_img);
            String name = c.getString(index_name);
            String summary = c.getString(index_summary);
            List<String> genres = Arrays.asList(
                    c.getString(index_genres).split(", ")
            );

            series.add(new Serie(api_id, name, new ArrayList<>(genres), summary, img));
        }

        c.close();
        Log.i(TAG, "Fetch parsed into an ArrayList of Serie.");
        return series;
    }
}
