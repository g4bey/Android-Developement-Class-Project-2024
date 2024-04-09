package gabey.space.activities.abtract;

import android.content.Intent;
import android.os.Bundle;

public class AbtractShowActivity extends AbtractBaseActivity {

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setId(i.getIntExtra("id", 1));
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }
}
