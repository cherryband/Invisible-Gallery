package neue.project.invisiblegallery.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileDescriptor;
import java.io.IOException;

import neue.project.invisiblegallery.EmptyListener;
import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.adapter.GalleryOverviewAdapter;
import neue.project.invisiblegallery.util.Util;

import static android.app.Activity.RESULT_CANCELED;

public class GalleryFragment extends Fragment implements EmptyListener {
    private static final int IMPORT_IMAGE = 100;
    private static final int TAKE_PHOTO = 200;
    private static ConstraintSet onNotEmptyConstraints = new ConstraintSet();

    private boolean isCameraAppInstalled = false;
    private FloatingActionButton importButton;
    private TextView emptyText;
    private RecyclerView overviewRecycler;
    private GalleryOverviewAdapter overviewAdapter;
    private ConstraintSet onEmptyConstraints;

    static {

    }

    @Override
    public View onCreateView (
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Intent dummy = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //isCameraAppInstalled = dummy.resolveActivity(requireActivity().getPackageManager()) != null;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_overview, container, false);
    }

    public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        importButton = view.findViewById(R.id.button_import);
        emptyText = view.findViewById(R.id.text_empty_gallery);
        overviewRecycler = view.findViewById(R.id.recycler_gallery_overview);
        //FloatingActionButton cameraButton = view.findViewById(R.id.button_camera);

        initRecycler();

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(gallery, IMPORT_IMAGE);
            }
        });
        /*
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = getPhotoUri(getCurrentTimestamp()+".jpg", requireContext());

                Uri fileProvider = FileProvider.getUriForFile(requireContext(), "neue.project.invisiblegallery", photo);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                if (isCameraAppInstalled) {
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                } else {
                    Snackbar.make(view, R.string.no_camera_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    private void initRecycler () {
        overviewAdapter = new GalleryOverviewAdapter(this);

    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        if (data != null) {
            final Uri photoUri = data.getData();

            try {
                final ContentResolver resolver = requireActivity().getContentResolver();
                ParcelFileDescriptor parcelFileDescriptor = resolver.openFileDescriptor(photoUri, "r");
                assert parcelFileDescriptor != null;
                final FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();


                new Thread(new Runnable(){
                    @Override
                    public void run () {
                        try {
                            Util.importFile(fileDescriptor, Util.getFileName(photoUri, resolver), requireContext().getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEmpty () {

    }

    @Override
    public void onNotEmpty () {

    }
}
