package neue.project.invisiblegallery.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryOverviewViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameText;

    public GalleryOverviewViewHolder (@NonNull TextView nameText) {
        super(nameText);
        this.nameText = nameText;
    }

    public TextView getNameText () {
        return nameText;
    }
}
