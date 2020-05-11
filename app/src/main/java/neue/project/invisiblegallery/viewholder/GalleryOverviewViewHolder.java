package neue.project.invisiblegallery.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import neue.project.invisiblegallery.R;

public class GalleryOverviewViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameText;

    public GalleryOverviewViewHolder (@NonNull View view) {
        super(view);
        this.nameText = view.findViewById(R.id.text_overview_name);
    }

    public TextView getNameText () {
        return nameText;
    }
}
