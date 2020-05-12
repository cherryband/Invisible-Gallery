package neue.project.invisiblegallery.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database (entities = {Image.class}, version = Database.VERSION)
public abstract class Database extends RoomDatabase{
    static final int VERSION = 2;
    public static final String NAME = "imagedb";

    public abstract ImageDao imageDao ();
    private static Database DATABASE;

    public static Database open(Context ctx){
        if (DATABASE == null){
            DATABASE = Room
                    .databaseBuilder(ctx.getApplicationContext(), Database.class, NAME)
                    .build();
        }

        return DATABASE;
    }
}
