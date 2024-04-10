package gabey.space.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gabey.space.R;
import gabey.space.model.CastMember;
import gabey.space.model.Serie;
import gabey.space.utils.StringUtils;
import gabey.space.viewHolder.CastViewHolder;
import gabey.space.viewHolder.SerieCardViewHolder;

public class CastMemberAdapter extends RecyclerView.Adapter<CastViewHolder> {
    private final String TAG = "OriginalDB@CastMemberAdapter";
    private final Context ctx;
    private final ArrayList<CastMember> castMembers;

    public CastMemberAdapter(Context ctx, ArrayList<CastMember> castMembers) {
        this.ctx = ctx;
        this.castMembers = castMembers;
    }

    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_cell, parent, false);
        return new CastViewHolder(view);
    }

    public void onBindViewHolder(CastViewHolder holder, int position) {
        Log.i(TAG, "Binding view holder at position: " + position);

        CastMember castMember = castMembers.get(position);
        holder.name.setText(castMember.getName());
        holder.character.setText(castMember.getCharacter());

        // downloads image from a different thread
        Picasso.get().load(castMember.getImg()).into(holder.img);
    }

    public int getItemCount() {
        return castMembers.size();
    }
}
