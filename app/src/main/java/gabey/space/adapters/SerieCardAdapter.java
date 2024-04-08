package gabey.space.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gabey.space.R;
import gabey.space.model.Serie;
import gabey.space.viewHolder.SerieCardViewHolder;

public class SerieCardAdapter extends RecyclerView.Adapter<SerieCardViewHolder> {

    private Context ctx;
    private ArrayList<Serie> series;

    public SerieCardAdapter(Context ctx, ArrayList<Serie> series) {
        this.ctx = ctx;
        this.series = series;
    }

    public SerieCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_serie, parent, false);
        return new SerieCardViewHolder(view);
    }

    public void onBindViewHolder(SerieCardViewHolder holder, int position) {
        Serie serie = series.get(position);
        holder.name.setText(serie.getName());
        holder.sumarry.setText(serie.getSummary());
        holder.genres.setText(serie.getGenresAsString());
        Picasso.get().load(serie.getImg()).into(holder.img);
    }

    public int getItemCount() {
        return series.size();
    }
}
