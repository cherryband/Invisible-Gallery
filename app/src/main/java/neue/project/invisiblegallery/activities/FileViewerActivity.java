package neue.project.invisiblegallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import neue.project.invisiblegallery.R;
import neue.project.invisiblegallery.data.Image;
import neue.project.invisiblegallery.fragments.FileViewerFragment;

public class FileViewerActivity extends SecureActivity {
    FileViewerFragment fileViewer;

    @Override
    protected void onCreate (Bundle prevState) {
        super.onCreate(prevState);
        setContentView(R.layout.activity_file_view);
        Toolbar toolbar = findViewById(R.id.toolbar_file_view);
        setSupportActionBar(toolbar);

        initFragments(getImage());
    }

    private Image getImage () {
        Intent intent = getIntent();
        return intent.getParcelableExtra(Image.IMAGE_PARCEL);
    }

    private void initFragments (Image startImage) {
        fileViewer = FileViewerFragment.newInstance(startImage);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_file_view, fileViewer)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
