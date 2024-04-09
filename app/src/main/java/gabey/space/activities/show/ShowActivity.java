package gabey.space.activities.show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;
import gabey.space.db.DBManager;
import gabey.space.model.Serie;
import gabey.space.utils.ErrorDialogHelper;
import gabey.space.utils.HttpHelper;
import gabey.space.utils.StringUtils;

public class ShowActivity extends AbtractShowActivity {
    private final String TAG = "OriginalDB@ShowActivity";
    private ImageView showImage;
    private TextView showName, showDesc, showGenres, showPremiered, showEnded,
            showStatus, showType, showLang, showNetwork, showRating, showRuntime;
    private TableLayout seasonTable;

    private Serie serie;
    boolean randomMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        Toolbar toolbar = findViewById(R.id.showNavigation);
        toolbar.setTitle("Informations");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        Intent i = getIntent();
        randomMode = i.getBooleanExtra("randomMode", false);

        if (randomMode) {
            final int max = 70000;
            Random random = new Random();
            setId(random.nextInt(max + 1));
        }

        findAllViews();

        // The start will be filled if the show is faved in DB.
        if (getDbManager().showIsFaved(getid())) {
            toolbar.getMenu()
                    .findItem(R.id.show_fav)
                    .setIcon(R.drawable.baseline_star_24);
        } else {
            toolbar.getMenu()
                    .findItem(R.id.show_fav)
                    .setIcon(R.drawable.baseline_star_outline_24);
        }

        try {
            loadMainInfoFromUrl();
            loadSeasonTable();
        } catch (JSONException e) {
            // throw new RuntimeException(e);
            this.finish();
        }
    }

    private void findAllViews() {
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
        showRuntime = findViewById(R.id.show_runtime);
        showRating = findViewById(R.id.show_rating);
        seasonTable = findViewById(R.id.seasonTable);
    }

    private void loadMainInfoFromUrl() throws JSONException {
        final String endpoint = "https://api.tvmaze.com/shows/";
        final String url = endpoint + getid();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONObject response = new JSONObject(HttpHelper.get(url));

                if (response.length() == 0) {
                    handler.post(() -> {
                        ErrorDialogHelper.showErrorDialog(
                                this, "This show doesn't exist!", this::finish
                        );
                    });
                    Log.w(TAG,"Show appeared on main page but wasn't found by API.");
                }

                // Insertion the information
                handler.post(()->{
                    try {
                        parseAndDisplayMainInfo(response);
                    } catch (JSONException e) {
                        Log.w(TAG, "Failed to parse JSON");
                        Log.w(TAG, e);
                        // Returning to the previous activity.
                        ErrorDialogHelper.showErrorDialog(
                                this, "Couldn't load data!", this::finish
                        );
                    }
                });
            } catch (JSONException e) {
                // this is not normal.
                handler.post(
                    () -> {
                        // Looking for random ID leads to a bunch of error
                        // TODO: Create a list of "OK" id.
                        if (randomMode) {
                            this.recreate();
                        } else {
                            ErrorDialogHelper.showErrorDialog(
                                    this, "An unexpected error occured.", () -> {
                                        this.finish();
                                    }
                            );
                        }
                    }
                );
                Log.w(TAG, "Couldn't get content for " + url);
            }
        });

    }

    private void loadSeasonTable() throws JSONException {
        final String base = "https://api.tvmaze.com/shows/";
        final String url = base + getid() + "/seasons";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONArray response = new JSONArray(HttpHelper.get(url));

                if (response.length() == 0) {
                    handler.post(() -> {
                        seasonTable.setVisibility(View.GONE);
                    });
                    Log.w(TAG,"Can't find seasons for this show.");
                }

                // Insertion the information
                handler.post(()->{
                    try {
                        parseAndLoadSeasonTable(response);
                    } catch (JSONException e) {
                        Log.w(TAG, "Failed to parse JSON");
                        Log.w(TAG, e);
                        handler.post(() -> {
                            seasonTable.setVisibility(View.GONE);
                        });
                    }
                });
            } catch (JSONException e) {
                // this is not normal.
                handler.post(
                        () -> {
                            seasonTable.setVisibility(View.GONE);
                            Log.w(TAG, "Couldn't fetch seasons for " + url);
                            Log.w(TAG, e);
                        }
                );
                Log.w(TAG, "Couldn't get content for " + url);
            }
        });

    }

    private void parseAndDisplayMainInfo(JSONObject response) throws JSONException {
        String name = response.getString("name");
        String type = response.getString("type");
        String status = response.getString("status");

        // getting genres
        ArrayList<String> genres = new ArrayList<>();
        JSONArray genresJSON = response.getJSONArray("genres");

        for (int i = 0; i < genresJSON.length(); i++) {
            genres.add(genresJSON.getString(i));
        }

        String language = "No summary yet!";
        if(!response.getString("language").equals("null")) {
            language = response.getString("language");
        }

        String network = "Unknown!";
        if(!response.getString("network").equals("null")) {
            network = response.getJSONObject("network").getString("name");
        }

        String summary = "No summary yet!";
        if(!response.getString("summary").equals("null")) {
            summary = response.getString("summary");
        }

        String premiered = "Unknown";
        if(!response.getString("premiered").equals("null")) {
            premiered = response.getString("premiered");
        }

        String runtime = "Unknown";
        if(!response.getString("runtime").equals("null")) {
            runtime = response.getString("runtime");
        }

        String ended = "";
        if(!response.getString("ended").equals("null")) {
            ended = response.getString("ended");
        }

        String rating = "No rating yet";
        if(!response.getJSONObject("rating").getString("average").equals("null")) {
            rating = response.getJSONObject("rating").getString("average");
        }

        // default image is necessary to avoid crashing of picasso.
        String img;
        try {
            img = response.getJSONObject("image").getString("medium");
        } catch (JSONException e) {
            img = getResources().getString(R.string.default_serie_picture);
        }

        this.serie = new Serie(getid(), name, genres, summary, img);

        this.showName.setText(name);
        this.showDesc.setText(StringUtils.removeHtmlTags(summary));
        this.showGenres.setText(StringUtils.joinAsString(genres, ", "));
        this.showPremiered.setText(premiered);
        this.showEnded.setText(ended);
        this.showStatus.setText(status);
        this.showType.setText(type);
        this.showLang.setText(language);
        this.showNetwork.setText(network);
        this.showRuntime.setText(runtime);
        this.showRating.setText(rating);

        // downloads image from a different thread
        Picasso.get().load(img).into(this.showImage);
    }

    private void parseAndLoadSeasonTable(JSONArray seasons) throws JSONException {

        for (int i = 0; i < seasons.length(); i++) {
            JSONObject season = seasons.getJSONObject(i);
            String no = season.getString("number");

            String episodes = "Unknown";
            if(!season.getString("episodeOrder").equals("null")) {
                episodes = season.getString("episodeOrder");
            }

            String debut = "Unknown";
            if(!season.getString("premiereDate").equals("null")) {
                debut = season.getString("premiereDate");
            }

            String ended = "";
            if(!season.getString("endDate").equals("null")) {
                ended = season.getString("endDate");
            }

            TableRow tr = new TableRow(this);
            TextView seasonNo = new TextView(this);
            TextView seasonEpisodes = new TextView(this);
            TextView seasonDebut = new TextView(this);
            TextView seasonEnded = new TextView(this);
            seasonNo.setText(no);
            seasonNo.setGravity(Gravity.CENTER);
            seasonEpisodes.setText(episodes);
            seasonEpisodes.setGravity(Gravity.CENTER);
            seasonDebut.setText(debut);
            seasonDebut.setGravity(Gravity.CENTER);
            seasonEnded.setText(ended);
            seasonEnded.setGravity(Gravity.CENTER);

            tr.addView(seasonNo);
            tr.addView(seasonEpisodes);
            tr.addView(seasonDebut);
            tr.addView(seasonEnded);

            seasonTable.addView(tr);
        }
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
        // if faved -> Removing from database, then changing the icon.
        // if no faved -> Adding into database, then changing the icon.
        else if (itemid == R.id.show_fav) {
            Toolbar toolbar = findViewById(R.id.showNavigation);

            if(getDbManager().showIsFaved(getid())) {
                getDbManager().unfavThisSerie(serie);
                toolbar.getMenu().findItem(R.id.show_fav).setIcon(R.drawable.baseline_star_outline_24);
                Toast.makeText(this, "The serie has been removed from your favorites.", Toast.LENGTH_SHORT).show();
            } else {
                getDbManager().favThisSerie(serie);
                toolbar.getMenu().findItem(R.id.show_fav).setIcon(R.drawable.baseline_star_24);
                Toast.makeText(this, "Adding the serie to your favorites", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }
}
