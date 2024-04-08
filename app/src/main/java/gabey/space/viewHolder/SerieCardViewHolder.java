package gabey.space.viewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import gabey.space.R;
import gabey.space.activities.NoInternetScreen;

public class SerieCardViewHolder  extends RecyclerView.ViewHolder {

    public ImageView img;
    public TextView name, sumarry, genres;

    public SerieCardViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.serie_card_name);
        sumarry = itemView.findViewById(R.id.serie_card_summary);
        genres = itemView.findViewById(R.id.serie_card_genres);
        img = itemView.findViewById(R.id.serie_card_img);

        itemView.setOnClickListener(view -> {
            view.getContext().startActivity(
                    new Intent(view.getContext(), NoInternetScreen.class)
            );
        });
    }
}
