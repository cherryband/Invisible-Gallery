package neue.project.invisiblegallery.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity
public class Image implements Parcelable {
    @PrimaryKey
    @NonNull
    private final String imagePath;
    @ColumnInfo (name = "imageName")
    private final String imageName;
    @ColumnInfo (name = "thumbnailPath")
    private final String thumbnailPath;

    @Ignore
    public static final String IMAGE_PARCEL = "neue.project.insivislegallery.data.Image";

    protected Image (Parcel in) {
        this(in.readString(), in.readString(), in.readString());
    }

    public Image (String imageName, String imagePath, String thumbnailPath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(imageName);
        dest.writeString(imagePath);
        dest.writeString(thumbnailPath);
    }

    @Override
    public int describeContents () {
        return 0;
    }

    public static final Creator <Image> CREATOR = new Creator <Image>() {
        @Override
        public Image createFromParcel (Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray (int size) {
            return new Image[size];
        }
    };

    public String getImagePath () {
        return imagePath;
    }

    public String getImageName () {
        return imageName;
    }

    public String getThumbnailPath () {
        return thumbnailPath;
    }

    @Override
    public boolean equals (@Nullable Object obj) {
        if (obj instanceof Image){
            return getImagePath().equals(((Image) obj).getImagePath());
        }
        return false;
    }
}

