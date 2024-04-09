package gabey.space.activities.show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;
import gabey.space.model.Serie;
import gabey.space.utils.ErrorDialogHelper;
import gabey.space.utils.HttpHelper;

public class ShowActivity extends AbtractShowActivity {
    private final String TAG = "OriginalDB@ShowActivity";
    private ImageView showImage;
    private TextView showName;
    private TextView showDesc;
    private TextView showGenres;
    private TextView showPremiered;
    private TextView showEnded;
    private TextView showStatus;
    private TextView showType;
    private TextView showLang;
    private TextView showNetwork;
    private TableLayout seasonTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        Toolbar toolbar = findViewById(R.id.showNavigation);
        toolbar.setTitle("Informations");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        showImage = findViewById(R.id.show_image);
        showName = findViewById(R.id.show_name);
        showDesc = findViewById(R.id.show_desc);
        showGenres = findViewById(R.id.show_genres);
        showPremiered = findViewById(R.id.show_premiered);
        showEnded = findViewById(R.id.show_ended);
        showStatus = findViewById(R.id.show_status);
        showType = findViewById(R.id.show_type);
        showLang = findViewById(R.id.show_lang);
        showNetwork = findViewById(R.id.show_network);
        seasonTable = findViewById(R.id.seasonTable);
    }

    private void loadFromUrl() throws JSONException {
        final String endpoint = "https://api.tvmaze.com/search/shows/";
        final String url = endpoint + getid();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONObject response = new JSONObject(HttpHelper.get(url));

                if (response.length() == 0) {
                    Log.i(TAG,"No Result found!");
                }

                // Insertion the information
                handler.post(()->{
                    try {
                        String name = response.getString("name");
                    } catch (JSONException e) {
                        Log.w(TAG, "Failed to parse JSON");
                        ErrorDialogHelper.showErrorDialog(
                                this, "A critical error occured.", this::finish
                        );
                    }
                });
            } catch (JSONException e) {
                Log.w(TAG, Objects.requireNonNull(e.getMessage()));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if (itemid == R.id.show_episodes) {
            Intent i = new Intent(this, EpisodesActivity.class);
            i.putExtra("id", getid());
            startActivity(i);
        }

        else if (itemid == R.id.show_cast) {
            Intent i = new Intent(this, CastActivity.class);
            i.putExtra("id", getid());
            startActivity(i);
        }

        else if (itemid == R.id.show_images) {
            Intent i = new Intent(this, ImagesActivity.class);
            i.putExtra("id", getid());
            startActivity(i);
        }

        else if (itemid == R.id.go_back) {
            this.finish();
        }

        else if (itemid == R.id.show_fav) {

        }

        return true;
    }
}
