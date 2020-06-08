package neue.project.invisiblegallery.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import neue.project.invisiblegallery.data.Database;
import neue.project.invisiblegallery.data.Image;
import neue.project.invisiblegallery.data.ImageDao;

public class Util {
    public static Image importFile(Context ctx, FileDescriptor fileDescriptor, String name, Bitmap thumbnail) throws IOException {
        File externalFilesDir = ctx.getExternalFilesDir(null);
        File cacheDir = new File(externalFilesDir, ".thumbnails");
        if (! cacheDir.exists() && ! cacheDir.mkdirs()){
            Log.e("inspector_mole", "Cannot create thumbnail directory; Storage full?");
        }

        String filename = UUID.randomUUID().toString();
        File out = new File(externalFilesDir, filename);
        File cache = new File(cacheDir, filename);

        try (OutputStream outStream = new FileOutputStream(cache)) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
        }

        try (InputStream in = new FileInputStream(fileDescriptor)) {
            try (OutputStream outStream = new FileOutputStream(out)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                }
            }
        }

        Image newImg = new Image(name, out.getAbsolutePath(), cache.getAbsolutePath());
        Database.open(ctx).imageDao().insert(newImg);
        return newImg;
    }

    public static void deleteImage(Image image, Context context){
        new File(image.getImagePath()).delete();
        new File(image.getThumbnailPath()).delete();

        Database
                .open(context.getApplicationContext())
                .imageDao()
                .delete(image);
    }

    public static Image renameImage(Context context, Image image, String newName) {
        ImageDao db = Database
                .open(context.getApplicationContext())
                .imageDao();
        boolean nameAvailable = db.findByName(newName).isEmpty();
        Image renamed = null;

        if (nameAvailable) {
            db.rename(image.getImagePath(), newName);
            renamed = db.findByName(newName).get(0);
        }

        return renamed;
    }

    @SuppressLint ("NewApi")
    public static String getFileName (Uri uri, ContentResolver resolver) {
        try (Cursor returnCursor = resolver.query(uri, null, null, null)) {
            assert returnCursor != null;
            returnCursor.moveToFirst();
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            return removeExtension(returnCursor.getString(nameIndex));
        }
    }

    public static Bitmap getThumbnail(Uri uri, ContentResolver resolver) throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
        //bmp.recycle();
        return ThumbnailUtils.extractThumbnail(bmp, 420, 420);
    }

    // Based on supported image extensions of Android: https://developer.android.com/guide/topics/media/media-formats#image-formats
    public static String removeExtension(String filename){
        return filename.replaceFirst("\\.(bmp|gif|jpe?g|png|webp|hei[cf])$", "");
    }

    public static String getCurrentTimestamp() {
        Date now = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(now);
    }
}
