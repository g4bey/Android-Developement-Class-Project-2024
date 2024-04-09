package gabey.space.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import gabey.space.R;

public class ShowActivity extends AbtractShowActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);

        Toolbar toolbar = findViewById(R.id.showNavigation);
        toolbar.setTitle("Informations");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if (itemid == R.id.show_episodes) {
            Intent i = new Intent(this, ShowEpisodesActivity.class);
            i.putExtra("id", getid());
            startActivity(i);
        }

        else if (itemid == R.id.show_cast) {
            Intent i = new Intent(this, ShowCastActivity.class);
            i.putExtra("id", getid());
            startActivity(i);
        }

        else if (itemid == R.id.show_images) {
            Intent i = new Intent(this, ShowImagesActivity.class);
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
