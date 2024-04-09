package gabey.space.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import gabey.space.R;

public class ShowImagesActivity extends AbtractShowActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_layout);

        Toolbar toolbar = findViewById(R.id.returnNavigation);
        toolbar.setTitle("Images");
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemid = item.getItemId();

        if (itemid == R.id.go_back) {
            this.finish();
        }

        return true;
    }

}
