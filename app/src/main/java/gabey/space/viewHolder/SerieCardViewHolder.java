package gabey.space.viewHolder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import gabey.space.R;
import gabey.space.activities.ShowActivity;

public class SerieCardViewHolder  extends RecyclerView.ViewHolder {
    private final String TAG = "OriginalDB@SerieCardViewHolder";
    public ImageView img;
    public TextView name, sumarry, genres, show_id;

    public SerieCardViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.serie_card_name);
        sumarry = itemView.findViewById(R.id.serie_card_summary);
        genres = itemView.findViewById(R.id.serie_card_genres);
        img = itemView.findViewById(R.id.serie_card_img);
        show_id = itemView.findViewById(R.id.show_id);

        itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), ShowActivity.class);
            Integer  id = Integer.valueOf((String) show_id.getText());
            i.putExtra("id", id);
            view.getContext().startActivity(i);
        });

        Log.i(TAG, "Created new view holder: " + this);
    }
}
