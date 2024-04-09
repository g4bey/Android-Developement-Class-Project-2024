package gabey.space.activities.error_pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gabey.space.R;
import gabey.space.receivers.InternetConnectionReceiver;


public class NoInternetActivity extends AppCompatActivity {

    private static final String TAG = "OriginalDB@NoInternetScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
    }

    public void retryConnecting(View view) {
        boolean isOnline = InternetConnectionReceiver.isOnline(this);

        if (!isOnline) {
            Toast.makeText(this, "Not connected to the internet.", Toast.LENGTH_SHORT).show();
        } else {
            this.finish();
        }

    }
}