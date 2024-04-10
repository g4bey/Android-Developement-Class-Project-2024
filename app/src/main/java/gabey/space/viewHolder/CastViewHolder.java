package gabey.space.viewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import gabey.space.R;

public class CastViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = "OriginalDB@CastViewHolder";
    public ImageView img;
    public TextView name, character;

    public CastViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.personName);
        img = itemView.findViewById(R.id.personImage);
        character = itemView.findViewById(R.id.personCharacter);

        Log.i(TAG, "Created new view older: " + this);
    }
}
