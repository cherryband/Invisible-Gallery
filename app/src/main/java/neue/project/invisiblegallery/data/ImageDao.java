package neue.project.invisiblegallery.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert
    void insert (Image... images);

    @Delete
    void delete (Image image);

    @Query("DELETE FROM image WHERE imageName LIKE :imageName")
    void delete (String imageName);

    @Query ("SELECT * FROM image WHERE imageName LIKE :imageName")
    List <Image> findByName (String imageName);

    @Query ("SELECT * FROM image")
    List <Image> getAll ();
}
