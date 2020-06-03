package neue.project.invisiblegallery.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.data.Image;

public class FileViewerFragment extends Fragment {
    private Image image;
    private ImageView imageView;
    private final Handler setImage = new Handler();
    private Snackbar errorSnackBar;

    public static FileViewerFragment newInstance(Image image){
        FileViewerFragment fragment = new FileViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Image.IMAGE_PARCEL, image);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getImage();
    }

    private Image getImage () {
        return (Image) requireArguments().getParcelable(Image.IMAGE_PARCEL);
    }

    @Override
    public View onCreateView (
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_view, container, false);
    }

    public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.image_file_view);
        errorSnackBar = Snackbar
                .make(view, "Error while loading image.", Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.colorBackgroundLight));

        loadImage();
    }

    private void loadImage () {
        new Thread(new Runnable(){
            @Override
            public void run () {
                File imgFile = new File(image.getImagePath());
                if (!imgFile.exists()){
                    setImage.post(new Runnable() {
                        @Override
                        public void run () {
                            errorSnackBar.show();
                        }
                    });
                } else {
                    final Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    setImage.post(new Runnable() {
                        @Override
                        public void run () {
                            imageView.setImageBitmap(bmp);
                        }
                    });
                }
            }
        }).start();
    }
}
