package neue.project.invisiblegallery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import neue.project.invisiblegallery.EmptyListener;
import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.data.Image;
import neue.project.invisiblegallery.util.ImageDiffCallback;
import neue.project.invisiblegallery.viewholder.GalleryOverviewViewHolder;

public class GalleryOverviewAdapter extends RecyclerView.Adapter <GalleryOverviewViewHolder> {
    List <Image> imageList = new ArrayList <>();
    EmptyListener emptyListener;
    public GalleryOverviewAdapter(EmptyListener emptyListener){
        this.emptyListener = emptyListener;
    }

    @NonNull
    @Override
    public GalleryOverviewViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.viewholder_image_preview, parent, false);

        return new GalleryOverviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull GalleryOverviewViewHolder holder, int position) {
        holder.setImage(imageList.get(position));
    }

    @Override
    public int getItemCount () {
        return imageList.size();
    }

    public boolean isEmpty () {
        return imageList.isEmpty();
    }

    public void add(Image image){
        int index = getItemCount();
        if (index < 1){
            emptyListener.onNotEmpty();
        }
        imageList.add(image);

        notifyItemInserted(index);
    }

    public void addAll(List<Image> images){
        int index = getItemCount();
        if (index < 1){
            emptyListener.onNotEmpty();
        }
        imageList.addAll(images);

        notifyItemRangeInserted(index, images.size());
    }

    public void remove(Image image){
        if (! imageList.contains(image)) return;
        int index = imageList.indexOf(image);

        imageList.remove(image);
        if (getItemCount() < 1){
            emptyListener.onEmpty();
        }

        notifyItemRemoved(index);
    }


    public void refresh (List <Image> images) {
        final ImageDiffCallback callback = new ImageDiffCallback(imageList, images);
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        if (isEmpty() && ! images.isEmpty()){
            emptyListener.onNotEmpty();
        } else if (! isEmpty() && images.isEmpty()){
            emptyListener.onEmpty();
        }

        imageList.clear();
        imageList.addAll(images);
        result.dispatchUpdatesTo(this);
    }
}
