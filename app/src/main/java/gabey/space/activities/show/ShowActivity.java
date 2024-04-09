package gabey.space.activities.show;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;

public class ShowActivity extends AbtractShowActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        Toolbar toolbar = findViewById(R.id.showNavigation);
        toolbar.setTitle("Informations");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
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
