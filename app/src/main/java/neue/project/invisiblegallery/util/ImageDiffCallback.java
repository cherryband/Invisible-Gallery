package neue.project.invisiblegallery.util;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import neue.project.invisiblegallery.data.Image;

public class ImageDiffCallback extends DiffUtil.Callback {
    private List<Image> oldImages, newImages;

    public ImageDiffCallback(List <Image> oldImages, List<Image> newImage){
        this.oldImages = oldImages;
        this.newImages = newImage;
    }

    @Override
    public int getOldListSize () {
        return oldImages.size();
    }

    @Override
    public int getNewListSize () {
        return newImages.size();
    }

    @Override
    public boolean areItemsTheSame (int oldItemPosition, int newItemPosition) {
        Image oldImage = oldImages.get(oldItemPosition);
        Image newImage = newImages.get(newItemPosition);
        return oldImage.equals(newImage);
    }

    @Override
    public boolean areContentsTheSame (int oldItemPosition, int newItemPosition) {
        String oldName = oldImages.get(oldItemPosition).getImageName();
        String newName = newImages.get(newItemPosition).getImageName();
        return oldName.equals(newName);
    }
}
