package gabey.space.activities.show;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import gabey.space.R;
import gabey.space.activities.abtract.AbtractShowActivity;

public class CastActivity extends AbtractShowActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cast_layout);

        Toolbar toolbar = findViewById(R.id.returnNavigation);
        toolbar.setTitle("Cast");
        toolbar.hideOverflowMenu();
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
