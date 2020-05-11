package neue.project.invisiblegallery.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Image implements Parcelable {
    @PrimaryKey
    @NonNull
    private final String imagePath;
    @ColumnInfo (name = "imageName")
    private final String imageName;

    protected Image (Parcel in) {
        this(in.readString(), in.readString());
    }

    public Image (String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
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
}

