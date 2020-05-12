package neue.project.invisiblegallery.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.activity.FileViewerActivity;
import neue.project.invisiblegallery.data.Image;

public class GalleryOverviewViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameText;
    private final CardView previewCard;
    private final ImageView previewImage;
    private Image image;
    private final Handler setImage = new Handler();

    public GalleryOverviewViewHolder (@NonNull View view) {
        super(view);
        this.previewCard = view.findViewById(R.id.card_preview);
        this.nameText = view.findViewById(R.id.text_preview_name);
        this.previewImage = view.findViewById(R.id.image_preview);

        previewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Context ctx = view.getContext();
                Intent openImage = new Intent(ctx, FileViewerActivity.class);
                openImage.putExtra(Image.IMAGE_PARCEL, image);
                ctx.startActivity(openImage);
            }
        });
    }

    public Image getImage () {
        return image;
    }

    public void setImage (Image image) {
        this.image = image;
        nameText.setText(image.getImageName());
        loadThumbnail();
    }

    private void loadThumbnail () {
        new Thread(new Runnable(){
            @Override
            public void run () {
                File imgFile = new File(image.getThumbnailPath());
                if (imgFile.exists()){
                    final Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    setImage.post(new Runnable() {
                        @Override
                        public void run () {
                            previewImage.setImageBitmap(bmp);
                        }
                    });
                }
            }
        }).start();
    }
}
