package gabey.space.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import gabey.space.R;
import gabey.space.adapters.SerieCardAdapter;
import gabey.space.model.Serie;
import gabey.space.utils.ErrorDialogHelper;
import gabey.space.utils.HttpHelper;


public class BasicSearchFragment extends Fragment {
    private final String TAG = "OriginalDB@BasicSearchFragment";

    public BasicSearchFragment() {
        super(R.layout.fragment_basic_search);
    }

    private RecyclerView recyclerView;
    private SerieCardAdapter serieCardAdapter;
    private ArrayList<Serie> series;

    private SearchView searchBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        series = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchBar.clearFocus();
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBar =  view.findViewById(R.id.serieSearchBar);
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (textIsValide(query)) {
                    try {
                        callApiThenDisplay(query);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Query is too short!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    series.clear();
                }
                return false;
            }

            public boolean textIsValide(String query) {
                return query.length() > 2;
            }
        });

        recyclerView =  view.findViewById(R.id.searchRecyclerView);
        serieCardAdapter = new SerieCardAdapter(getContext(), series);
        recyclerView.setAdapter(serieCardAdapter);
    }

    /*
        Fetches the different series from the API.
        Handles empty response and make sure to disable / enable the search to avoid spamming.
        Closes the app if a critical error occurs.
     */
    private void callApiThenDisplay(String query) throws JSONException {
        searchBar.setSubmitButtonEnabled(false);

        final String endpoint = "https://api.tvmaze.com/search/shows?q=";
        final String url = endpoint + query;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONArray response = new JSONArray(HttpHelper.get(url));

                // Right API call but no result found.
                if (response.length() == 0) {
                    handler.post(()->{
                        Toast.makeText(getContext(), "No result found!", Toast.LENGTH_SHORT).show();
                        searchBar.setSubmitButtonEnabled(true);
                    });
                } else {
                    // The goal is to swap the result after treatment.
                    // First disabling the search then re-enabling.
                    handler.post(()->{
                        try {
                            ArrayList<Serie> futureSeries = parseSeries(response);
                            series.clear();
                            for (Serie serie : futureSeries) {
                                series.add(serie);
                                this.serieCardAdapter.notifyItemChanged(series.size() -1);
                            }
                        } catch (JSONException e) {
                            Log.w(TAG, Objects.requireNonNull(e.getMessage()));
                            throw new RuntimeException(e);
                        }
                        searchBar.setSubmitButtonEnabled(true);
                    });
                }
            } catch (JSONException e) {
                // this is not normal.
                handler.post(
                        () -> {
                            ErrorDialogHelper.showErrorDialog(
                                    getContext(), "An unexpected error occured.", () ->
                                    {
                                        this.getActivity().finish();
                                        throw new RuntimeException(e);
                                    }
                            );
                        }
                );
                Log.w(TAG, "Couldn't get content for " + url);
            }
        });
    }

    private ArrayList<Serie> parseSeries(JSONArray s) throws JSONException {
        ArrayList<Serie> futureSeries = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            JSONObject root = s.getJSONObject(i);
            JSONObject serie = root.getJSONObject("show");
            String summary = serie.getString("summary");
            double score = root.getDouble("score");
            int id = serie.getInt("id");
            String name = serie.getString("name");

            // loading genres
            JSONArray genresJSON = serie.getJSONArray("genres");
            ArrayList<String> genres = new ArrayList<>();
            for (int j = 0; j < genresJSON.length(); j++) {
                genres.add(
                        genresJSON.getString(j)
                );
            }

            // making sure there's a default picture if none is provided.
            String img;
            try {
                img = serie.getJSONObject("image").getString("medium");
            } catch (JSONException e) {
                img = getResources().getString(R.string.default_serie_picture);
            }

            if(summary.equals("null")) {
                summary = "No summmary yet";
            }
            futureSeries.add(new Serie(id, name, genres, summary, img, score));
        }

        return futureSeries;
    }
}