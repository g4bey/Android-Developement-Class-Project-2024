package gabey.space.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;
import gabey.space.adapters.CastMemberAdapter;
import gabey.space.model.CastMember;
import gabey.space.utils.HttpHelper;

public class CastActivity extends AbtractShowActivity {
    private final String TAG = "OriginalDB@EpisodesActivity";
    CastMemberAdapter castMemberAdapter;
    ArrayList<CastMember> cast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cast_layout);

        // setting up the toolbar
        Toolbar toolbar = findViewById(R.id.returnNavigation);
        toolbar.setTitle("Cast");
        toolbar.hideOverflowMenu();
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        // setting up the cast grid adapter
        cast = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.castGrid);
        castMemberAdapter = new CastMemberAdapter(this, cast);
        recyclerView.setAdapter(castMemberAdapter);

        loadEpisodeIntoList();
    }

    public void loadEpisodeIntoList() {
        Log.i(TAG, "Fetching cast");
        final String endpoint = "https://api.tvmaze.com/shows/";
        final String url = endpoint + getid() + "/cast";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                JSONArray persons = new JSONArray(HttpHelper.get(url));
                handler.post(() -> {
                    try {
                        parseAndLoadCastMember(persons);
                        // castMemberAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.w(TAG, e);
                        Toast.makeText(this, "Couldn't load cast", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                });
            } catch (JSONException e) {
                //
            }
        });
    }

    private void parseAndLoadCastMember(JSONArray persons) throws JSONException {
        Log.i(TAG, "Parsing cast members");
        for (int i = 0; i < persons.length(); i++) {
            JSONObject person = persons.getJSONObject(i).getJSONObject("person");
            String name = person.getString("name");
            int id = person.getInt("id");

            String img;
            try {
                img = person.getJSONObject("image").getString("original");
            } catch (JSONException e) {
                img = getResources().getString(R.string.default_cast_picture);
            }

            JSONObject characterJson = persons.getJSONObject(i).getJSONObject("character");
            String character =  characterJson.getString("name");

            String finalImg = img;
            CastMember castMember = new CastMember(id, name, finalImg, character);
            cast.add(castMember);
            castMemberAdapter.notifyItemChanged(i);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if (itemid == R.id.go_back) {
            this.finish();
        }

        return true;
    }


}
