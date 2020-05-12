package neue.project.invisiblegallery.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import neue.project.invisiblegallery.data.Database;
import neue.project.invisiblegallery.data.Image;

public class Util {
    public static Image importFile(FileDescriptor fileDescriptor, String name, Context ctx) throws IOException {
        File externalFilesDir = ctx.getExternalFilesDir(null);
        File out = new File(externalFilesDir, name);

        try (InputStream in = new FileInputStream(fileDescriptor)) {
            try (OutputStream outStream = new FileOutputStream(out)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                }
                Image newImg = new Image(name, out.getPath());
                Database.open(ctx).imageDao().insert(newImg);
                return newImg;
            }
        }
    }

    @SuppressLint ("NewApi")
    public static String getFileName (Uri uri, ContentResolver resolver) {
        try (Cursor returnCursor = resolver.query(uri, null, null, null)) {
            assert returnCursor != null;
            returnCursor.moveToFirst();
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            return returnCursor.getString(nameIndex);
        }
    }

    public static String getCurrentTimestamp() {
        Date now = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(now);
    }
}
