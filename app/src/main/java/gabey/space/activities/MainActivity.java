package gabey.space.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractBaseActivity;
import gabey.space.fragments.SeriesFragment;
import gabey.space.fragments.FavoriteFragment;

public class MainActivity extends AbtractBaseActivity {
    private static final String TAG = "OriginalDB@MainActivity";
    BottomNavigationView bottomNavigation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "Inflating menu");
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.topNavigation);

        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        // only executed when created.
        if (savedInstanceState == null) {
            Log.i(TAG, "Initiating default fragment.");
            replaceFragment(new SeriesFragment());
            toolbar.setTitle("Series");
        }

        setSupportActionBar(toolbar);

        // event listener for bottom navigation
        bottomNavigation.setOnItemSelectedListener(
                (item) -> {
                    int currItem = item.getItemId();
                    if(currItem == R.id.BottomNavbarSeries) {
                        Log.i(TAG, "Switching to Series Fragment");
                        replaceFragment(new SeriesFragment());
                        toolbar.setTitle("Series");
                    }
                    else if(currItem == R.id.BottomNavbarFavorites) {
                        Log.i(TAG, "Switching to Favorites Fragment");
                        replaceFragment(new FavoriteFragment());
                        toolbar.setTitle("Favorites");
                    } else if (currItem == R.id.BottomNavbarRandom) {
                        Log.i(TAG, "Picking a random show!");
                        Intent i = new Intent(this, ShowActivity.class);
                        i.putExtra("randomMode", true);
                        startActivity(i);
                    }

                    return true;
                }
        );

    }

    /*
        Replaces current fragment.
     */
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.toggle_color_mode) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                Log.i(TAG, "Dark mode off");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                Log.i(TAG, "Dark mode on");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }

        return true;
    }
}
