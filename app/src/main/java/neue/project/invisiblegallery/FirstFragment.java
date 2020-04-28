package neue.project.invisiblegallery;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class FirstFragment extends Fragment{
    private static final int IMPORT_IMAGE=100;
    private static final int TAKE_PHOTO=200;
    private boolean isCameraAppInstalled = false;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Intent dummy = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        isCameraAppInstalled = dummy.resolveActivity(requireActivity().getPackageManager()) != null;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton importButton = view.findViewById(R.id.button_import);
        FloatingActionButton cameraButton = view.findViewById(R.id.button_camera);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, IMPORT_IMAGE);
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (isCameraAppInstalled) {
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                } else {
                    Snackbar.make(view, R.string.no_camera_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
