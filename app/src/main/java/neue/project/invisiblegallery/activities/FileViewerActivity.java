package neue.project.invisiblegallery.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.data.Image;
import neue.project.invisiblegallery.fragments.FileViewerFragment;
import neue.project.invisiblegallery.util.Util;

public class FileViewerActivity extends SecureActivity {
    FileViewerFragment fileViewer;
    private final Handler handler = new Handler();
    private Toolbar toolbar;

    private Image startImage;

    @Override
    protected void onCreate (Bundle prevState) {
        super.onCreate(prevState);
        setContentView(R.layout.activity_file_view);
        toolbar = findViewById(R.id.toolbar_file_view);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getImage();

        toolbar.setTitle(startImage.getImageName());
        initFragments();
    }

    private void getImage () {
        Intent intent = getIntent();
        startImage = intent.getParcelableExtra(Image.IMAGE_PARCEL);
    }

    private void initFragments () {
        fileViewer = FileViewerFragment.newInstance(startImage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_file_view, fileViewer)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                new AlertDialog
                        .Builder(this)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE)
                                    removeImageAndQuit(startImage);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            case R.id.action_rename:
                showRenameDialog();
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRenameDialog() {
        final EditText filename = new EditText(this);
        filename.setText(startImage.getImageName());

        new AlertDialog
                .Builder(this)
                .setMessage(R.string.alert_rename)
                .setView(filename)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newName = filename.getText().toString();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                rename(newName);
                            }
                        }).start();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void rename(String newName) {
        Image renamed = Util.renameImage(this, startImage, newName);

        final String toastMsg;
        if (renamed != null) {
            toastMsg = "Renamed";
            startImage = renamed;
        } else
            toastMsg = "Name already exists";

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast
                        .makeText(FileViewerActivity.this, toastMsg, Toast.LENGTH_SHORT)
                        .show();

                toolbar.setTitle(startImage.getImageName());
            }
        });
    }

    private void removeImageAndQuit(final Image image) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.deleteImage(image, getApplicationContext());
                finishInBg();
            }
        }).start();
    }

    private void finishInBg(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }
}
