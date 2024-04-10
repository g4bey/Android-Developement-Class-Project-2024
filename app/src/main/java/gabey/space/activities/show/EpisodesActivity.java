package gabey.space.activities.show;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;
import gabey.space.adapters.EpisodeAdapter;
import gabey.space.model.Episode;
import gabey.space.utils.HttpHelper;
import gabey.space.utils.StringUtils;

public class EpisodesActivity extends AbtractShowActivity {

    ListView episodes;
    Spinner seasonSpinner;
    List<String> seasonId;
    List<String> seasonLabel;
    EpisodeAdapter episodeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_layout);
        Toolbar toolbar = findViewById(R.id.returnNavigation);
        toolbar.setTitle("Episodes");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        seasonLabel = new ArrayList<>();
        seasonId = new ArrayList<>();

        // Creating the spinner.
        seasonSpinner = findViewById(R.id.seasonEpisodesChoice);
        seasonLabel.add("-- Select a season --");
        ArrayAdapter<String> seasonSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, seasonLabel);
        seasonSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonSpinnerAdapter);

        // fill the season spinner with the season labels.
        fillSeasonSpinnerAdapter();

        // set up the list adapter to later load the episodes.
        episodeAdapter = new EpisodeAdapter(this, new ArrayList<>());
        ListView listView = findViewById(R.id.seasonEpisodes);
        listView.setAdapter(episodeAdapter);

        // handles spinner events.
        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else {
                    int episode_id = Integer.parseInt(seasonId.get(position -1));
                    loadEpisodeIntoList(episode_id);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadEpisodeIntoList(int season_id) {
        final String endpoint = "https://api.tvmaze.com/seasons/";
        final String url = endpoint + season_id + "/episodes";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONArray episodes = new JSONArray(HttpHelper.get(url));

                    handler.post(() -> {
                        episodeAdapter.clear();
                    });

                    for (int i = 0; i < episodes.length(); i++) {
                        JSONObject episodeJson = episodes.getJSONObject(i);

                        String name = episodeJson.getString("name");
                        String summary = episodeJson.getString("summary");
                        int id = episodeJson.getInt("id");

                        String img;
                        try {
                            img = episodeJson.getJSONObject("image").getString("original");
                        } catch (JSONException e) {
                            img = getResources().getString(R.string.default_episode_picture);
                        }

                        String finalImg = img;
                        handler.post(() -> {
                            Episode episode = new Episode(name, StringUtils.removeHtmlTags(summary), id, finalImg);
                            episodeAdapter.add(episode);
                        });
                    }


            } catch (JSONException e) {
                //
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if (itemid == R.id.go_back) {
            this.finish();
        }

        return true;
    }

    public void fillSeasonSpinnerAdapter() {
        final String endpoint = "https://api.tvmaze.com/shows/";
        final String url = endpoint + getid() + "/seasons";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONArray seasons = new JSONArray(HttpHelper.get(url));
                    try {
                        for (int i = 0; i < seasons.length(); i++) {
                            String season_id = String.valueOf(seasons.getJSONObject(i).getInt("id"));
                            String season_number = String.valueOf(seasons.getJSONObject(i).getInt("number"));
                            seasonLabel.add(season_number);
                            seasonId.add(season_id);
                        }
                    }catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (seasons.length() == 0) {
                        handler.post(() -> {
                            Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT);
                            this.finish();
                        });
                    } else {
                        handler.post(() -> {
                            seasonSpinner.setSelection(0);
                            seasonSpinner.setSelected(true);
                        });

                    }
            } catch (JSONException e) {
                // throw new RuntimeException(e);
            }
        });
    }
}
