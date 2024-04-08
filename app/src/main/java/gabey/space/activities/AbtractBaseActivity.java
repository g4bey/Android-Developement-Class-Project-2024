package gabey.space.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import gabey.space.R;
import gabey.space.receivers.InternetConnectionReceiver;


public abstract class AbtractBaseActivity extends AppCompatActivity  {
    private final InternetConnectionReceiver connectionReceiver = new InternetConnectionReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectionReceiver);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}
