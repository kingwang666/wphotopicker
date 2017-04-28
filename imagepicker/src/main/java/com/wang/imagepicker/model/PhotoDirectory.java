package com.wang.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class PhotoDirectory implements Parcelable{

    public boolean select;

    public String id;

    public String coverPath;

    public String name;

    public long dateAdded;

    public List<Photo> photos = new ArrayList<>();

    public PhotoDirectory(){

    }

    protected PhotoDirectory(Parcel in) {
        select = in.readByte() != 0;
        id = in.readString();
        coverPath = in.readString();
        name = in.readString();
        dateAdded = in.readLong();
        photos = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<PhotoDirectory> CREATOR = new Creator<PhotoDirectory>() {
        @Override
        public PhotoDirectory createFromParcel(Parcel in) {
            return new PhotoDirectory(in);
        }

        @Override
        public PhotoDirectory[] newArray(int size) {
            return new PhotoDirectory[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDirectory)) return false;

        PhotoDirectory directory = (PhotoDirectory) o;

        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);

        return hasId && otherHasId && TextUtils.equals(id, directory.id) && TextUtils.equals(name, directory.name);

    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }

            return name.hashCode();
        }

        int result = id.hashCode();

        if (TextUtils.isEmpty(name)) {
            return result;
        }

        result = 31 * result + name.hashCode();
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (select ? 1 : 0));
        dest.writeString(id);
        dest.writeString(coverPath);
        dest.writeString(name);
        dest.writeLong(dateAdded);
        dest.writeTypedList(photos);
    }
}
