package com.wang.imagepicker.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.utils.PhotoPicker;

import java.io.File;
import java.io.Serializable;

public class Photo implements Parcelable, Cloneable{

    public boolean select;

    public int id;

    public String path;
    /**
     * 1 - 需要压缩，2 - 无需压缩，3 - 系统资源文件, 4-截图
     */
    public int type;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
        type = 1;
    }

    public Photo(int id, String path, int type) {
        this.id = id;
        this.path = path;
        this.type = type;
    }

    public Photo() {
        path = "";
        type = 1;
    }


    protected Photo(Parcel in) {
        select = in.readByte() != 0;
        id = in.readInt();
        path = in.readString();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (select ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeInt(type);
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
        if (photo.id == Extra.TAKE_PHOTO){
            String[] names = photo.path.split(File.separator);
            return path.endsWith(names[names.length - 1]);
        }
        else if (photo.id == Extra.CROP){
            return path.equals(photo.path);
        }
        else {
            return id == photo.id || path.equals(photo.path);
        }

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
