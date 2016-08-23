package com.wang.imagepicker.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable, Cloneable{

    public boolean select;

    public int id;

    public String path;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        select = in.readByte() != 0;
        id = in.readInt();
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (select ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
