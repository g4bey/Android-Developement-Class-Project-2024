package gabey.space.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import gabey.space.activities.NoInternetActivity;

public class InternetConnectionReceiver extends BroadcastReceiver {

    private final String TAG = "OriginalDB@InternetConnectionReceiver";
    public static Boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if (network == null) return false;

        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

        // Captures connectivity loss within reasonable time.
        return networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!InternetConnectionReceiver.isOnline(context)) {
            Log.d(TAG, "Network Has Gone Offline.");
            Intent i = new Intent(context, NoInternetActivity.class);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Network Change Captured");
        }
    }

    public interface ReceiverListener {
        void onNetworkChange(boolean isConnected);
    }

}