package gabey.space.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;

import gabey.space.R;
import gabey.space.adapters.SerieCardAdapter;
import gabey.space.db.DBManager;
import gabey.space.model.Serie;


public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private SerieCardAdapter serieCardAdapter;
    private ArrayList<Serie> series;
    private DBManager dbManager;
    Spinner sortOptions;
    SearchView searchBar;
    public FavoriteFragment() {
        super(R.layout.fragment_favorite);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        series = new ArrayList<>();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbManager = DBManager.getInstance(getContext());

        sortOptions = view.findViewById(R.id.sort_series);
        searchBar = view.findViewById(R.id.favoriteSearchBar);

        // Adding sort option for the spinner
        String[] spinnerOptions = new String[] {
                "Ascending", "Descending"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptions.setAdapter(adapter);

        // loading the entire list by default
        series = dbManager.getFavedSeries();

        // attach the serie
        recyclerView =  view.findViewById(R.id.favorite_recycle_view);
        serieCardAdapter = new SerieCardAdapter(getContext(), series);
        recyclerView.setAdapter(serieCardAdapter);

        // Called ONCE when activity is started
        // Sort series based on the name.
        sortOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Collections.sort(series);
                } else {
                    Collections.reverse(series);
                }

                serieCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // update the list as the user is typing.
            // It was necessary to take care of the current state
            // of the spinner here.
            // Especially since this method will remove items.
            @Override
            public boolean onQueryTextChange(String newText) {
                searchBar.setSubmitButtonEnabled(false);

                // starting from a clean state.
                ArrayList<Serie> baseline = dbManager.getFavedSeries();

                // only adding items that are necessary.
                ArrayList<Serie> filteredSeries = new ArrayList<>();
                for (int i = 0; i < baseline.size(); i++) {
                    Serie serie = baseline.get(i);
                    if (searchBar.getQuery().length() > 0) {
                        String q = searchBar.getQuery().toString().toLowerCase();
                        String title = serie.getName().toLowerCase();
                        if(title.contains(q)) {
                            filteredSeries.add(serie);
                        }
                    } else {
                        filteredSeries.add(serie);
                    }
                }

                // sorting the result depending on the pos.
                // only reversing if necessary
                int pos = sortOptions.getSelectedItemPosition();
                Collections.sort(filteredSeries);
                if (pos == 1) {
                    Collections.reverse(filteredSeries);
                }

                // emptying the list and notifying the adapter.
                // to force the update and prevent clashes from different events.
                series.clear();
                serieCardAdapter.notifyDataSetChanged();

                // Adding items that have been filtered.
                for (int i = 0; i < filteredSeries.size(); i++) {
                    series.add(filteredSeries.get(i));
                    serieCardAdapter.notifyItemInserted(i);
                }

                return false;
            }
        });
    }



}