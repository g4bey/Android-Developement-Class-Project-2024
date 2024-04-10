package gabey.space.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

import gabey.space.R;
import gabey.space.model.Episode;

public class EpisodeAdapter extends ArrayAdapter<Episode> {

    public EpisodeAdapter(Context ctx, List<Episode> episodes) {
        super(ctx, R.layout.episode_list_view, episodes);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Episode episode = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.episode_list_view, parent, false);
        }

        TextView episodeTitle = convertView.findViewById(R.id.episodeTitle);
        TextView episodeDesc = convertView.findViewById(R.id.episodeDesc);
        ImageView episodeImg = convertView.findViewById(R.id.imageEpisode);

        episodeTitle.setText(episode.getTitle());
        episodeDesc.setText(episode.getDescription());

        Picasso.get().load(episode.getImg()).into(episodeImg);


        return convertView;
    }

}
