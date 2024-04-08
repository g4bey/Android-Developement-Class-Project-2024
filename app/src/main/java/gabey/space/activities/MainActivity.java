package gabey.space.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gabey.space.R;
import gabey.space.fragments.BasicSearchFragment;
import gabey.space.fragments.FavoriteFragment;
import gabey.space.fragments.TriviaFragment;

public class MainActivity extends AbtractBaseActivity{

    BottomNavigationView bottomNavigation;
    BasicSearchFragment basicSearchFragment;
    FavoriteFragment favoriteFragment;
    TriviaFragment triviaFragment;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // using the default menu
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottomNavigationView);

        Toolbar toolbar = findViewById(R.id.topNavigation);

        basicSearchFragment = new BasicSearchFragment();
        favoriteFragment = new FavoriteFragment();
        triviaFragment = new TriviaFragment();

        if (savedInstanceState == null) {
            displayFragment(basicSearchFragment);
            toolbar.setTitle("Series");
        }

        setSupportActionBar(toolbar);

        // event listener for bottom navigation
        bottomNavigation.setOnItemSelectedListener(
                (item) -> {
                    int currItem = item.getItemId();
                    if(currItem == R.id.BottomNavbarSeries) {
                        displayFragment(basicSearchFragment);
                        toolbar.setTitle("Series");
                    }
                    else if(currItem == R.id.BottomNavbarFavorites) {
                        displayFragment(favoriteFragment);
                        toolbar.setTitle("Favorites");
                    }
                    else if(currItem == R.id.BottomNavbarTrivia) {
                        displayFragment(triviaFragment);
                        toolbar.setTitle("Trivia");
                    }

                    return true;
                }
        );

    }

    /*
        Hides every known fragmment then display the one selected.
     */
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    public void darkModeToggle(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.toggle_color_mode) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }

    }
}
