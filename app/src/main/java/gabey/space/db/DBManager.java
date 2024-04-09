package gabey.space.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


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
}
