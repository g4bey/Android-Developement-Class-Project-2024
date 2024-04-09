package gabey.space.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import gabey.space.R;
import gabey.space.receivers.InternetConnectionReceiver;


public abstract class AbtractBaseActivity extends AppCompatActivity  {
    private static final String TAG = "OriginalDB@AbtractBaseActivity";
    private final InternetConnectionReceiver connectionReceiver = new InternetConnectionReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
