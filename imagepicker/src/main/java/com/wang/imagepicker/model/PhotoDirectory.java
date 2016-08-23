package com.wang.imagepicker.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class PhotoDirectory {

    public boolean select;

    public String id;

    public String coverPath;

    public String name;

    public long dateAdded;

    public List<Photo> photos = new ArrayList<>();

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



}
