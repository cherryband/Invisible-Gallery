package neue.project.invisiblegallery.instrumented;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import neue.project.invisiblegallery.data.Database;
import neue.project.invisiblegallery.data.Image;
import neue.project.invisiblegallery.data.ImageDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseMockTest {
    Database db;
    ImageDao dao;

    @Before
    public void createDb(){
        Context ctx = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(ctx, Database.class).build();
        dao = db.imageDao();
    }

    @Ignore("Not a test")
    public boolean assertImageEquals(Image image1, Image image2){
        if (! image1.equals(image2)) return false;

        return image1.getImageName().equals(image2.getImageName()) &&
                image1.getThumbnailPath().equals(image2.getThumbnailPath());
    }

    @Test
    public void testAddition() throws Exception {
        Image image = new Image("imageName", "imagePath", "thumbnailPath");
        dao.insert(image);
        assertEquals(1, dao.getAll().size());
        Image image1 = dao.getAll().get(0);

        assertTrue(assertImageEquals(image, image1));
    }


    @Test
    public void testRemoval() throws Exception {
        Image image = new Image("imageName", "imagePath", "thumbnailPath");
        dao.insert(image);
        assertEquals(1, dao.getAll().size());

        dao.delete(image);
        assertEquals(0, dao.getAll().size());
    }


    @After
    public void closeDb() throws IOException {
        db.close();
    }
}