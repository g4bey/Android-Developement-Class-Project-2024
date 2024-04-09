package gabey.space.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gabey.space.R;
import gabey.space.model.Serie;
import gabey.space.utils.StringUtils;
import gabey.space.viewHolder.SerieCardViewHolder;

public class SerieCardAdapter extends RecyclerView.Adapter<SerieCardViewHolder> {
    private final String TAG = "OriginalDB@SerieCardAdapter";
    private final Context ctx;
    private final ArrayList<Serie> series;

    public SerieCardAdapter(Context ctx, ArrayList<Serie> series) {
        this.ctx = ctx;
        this.series = series;
    }

    public SerieCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_serie, parent, false);
        return new SerieCardViewHolder(view);
    }

    public void onBindViewHolder(SerieCardViewHolder holder, int position) {
        Log.i(TAG, "Binding view holder at position: " + position);

        Serie serie = series.get(position);
        holder.name.setText(serie.getName());
        holder.sumarry.setText(serie.getShortSummary());
        holder.genres.setText(StringUtils.joinAsString(serie.getGenres(), ", "));
        holder.show_id.setText(String.valueOf(serie.getId()));

        // downloads image from a different thread
        Picasso.get().load(serie.getImg()).into(holder.img);
    }

    public int getItemCount() {
        return series.size();
    }
}
