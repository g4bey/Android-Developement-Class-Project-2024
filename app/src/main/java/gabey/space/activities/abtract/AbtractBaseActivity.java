package gabey.space.activities.abtract;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import gabey.space.R;
import gabey.space.db.DBManager;
import gabey.space.receivers.InternetConnectionReceiver;


public abstract class AbtractBaseActivity extends AppCompatActivity  {
    private static final String TAG = "OriginalDB@AbtractBaseActivity";
    private final InternetConnectionReceiver connectionReceiver = new InternetConnectionReceiver();
    private DBManager dbManager;

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = DBManager.getInstance(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int darkMode = sharedPref.getInt(getString(R.string.color_mode), -1);

        if (darkMode == 2) {
            Log.i(TAG, "Setting up Dark Mode from preferences");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (darkMode == 1) {
            Log.i(TAG, "Setting up Light Mode from preferences");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            Log.i(TAG, "Setting up default light mode.");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Registering Connection Receiver");
        registerReceiver(connectionReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Unregistering Connection Receiver");
        unregisterReceiver(connectionReceiver);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}
